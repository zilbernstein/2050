package ru.digipeople.thingworx

import com.thingworx.communications.client.AndroidConnectedThingClient
import com.thingworx.communications.client.ClientConfigurator
import com.thingworx.communications.common.SecurityClaims
import com.thingworx.relationships.RelationshipTypes
import com.thingworx.types.collections.ValueCollection
import com.thingworx.types.primitives.BooleanPrimitive
import com.thingworx.types.primitives.NumberPrimitive
import com.thingworx.types.primitives.StringPrimitive
import io.reactivex.Single
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.digipeople.logger.LoggerFactory
import ru.digipeople.thingworx.filetransfer.FileTransferResult
import ru.digipeople.thingworx.filetransfer.FileTransferThing
import ru.digipeople.thingworx.model.base.ApiResponse
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Класс дл работы с системой BaseThingWorx.
 */
@Singleton
class BaseThingWorx @Inject constructor(private val thingWorxConfigProvider: ThingWorxConfigProvider) {
    /**
     * подключение логгирования
     */
    private val logger = LoggerFactory.getLogger(BaseThingWorx::class)

    private var client: AndroidConnectedThingClient? = null
    /**
     * соединение
     */
    suspend fun connect(username: String, password: String) = withContext(Dispatchers.IO) {
        connectSync(username, password)
    }

    fun connectRx(username: String, password: String): Single<Boolean> {
        return Single.fromCallable { connectSync(username, password) }
    }

    private fun connectSync(username: String, password: String): Boolean {
        val client = createClient(username, password)
        client.start()

        return if (client.waitForConnection(10000)) {
            this.client = client
            true
        } else {
            client.shutdown()
            false
        }
    }
    /**
     * разъединение
     */
    suspend fun disconnect() = withContext(Dispatchers.IO) {
        disconnectSync()
    }

    fun disconnectBlocking() {
        disconnectSync()
    }

    private fun disconnectSync() {
        client?.shutdown()
        client = null
    }
    /**
     * проверка соединения
     */
    fun isConnected(): Boolean {
        return client != null
    }

    suspend fun requestThingWorx(serviceName: String, request: Any? = null) = withContext(Dispatchers.IO) {
        val params = if (request == null) {
            emptyMap()
        } else {
            ThingWorxObjectMapper.map(request)
        }
        requestThingWorxSync(serviceName, params)
    }
    /**
     * запрос в фингворкс
     */
    fun requestThingWorxRx(serviceName: String, request: Any): Single<ApiResponse> {
        return requestThingWorxRx(serviceName, ThingWorxObjectMapper.map(request))
    }
    /**
     * запрос в фингворкс
     */
    fun requestThingWorxRx(serviceName: String, params: Map<String, Any>): Single<ApiResponse> {
        return Single.fromCallable { requestThingWorxSync(serviceName, params) }
    }

    /**
     * Выполненяет запрос к ThingWorx
     */
    private fun requestThingWorxSync(serviceName: String, params: Map<String, Any>): ApiResponse {
        val valueParams = ValueCollection()
        params.forEach { (key, value) ->
            when (value) {
                is String -> valueParams.put(key, StringPrimitive(value))
                is Number -> valueParams.put(key, NumberPrimitive(value))
            }
        }

        val client = client!!
        val isConnected = client.isConnected
        logger.trace("client.isConnected = $isConnected")
        if (!isConnected) {
            logger.trace("client.connect start")
            client.connect()
            logger.trace("client.connect end")
        }

        if (BuildConfig.DEBUG) {
            logger.trace("serviceName: $serviceName, params:\n$params")
        }

        val infoTable = client.invokeService(
                RelationshipTypes.ThingworxEntityTypes.Things,
                thingWorxConfigProvider.getConfig().library,
                serviceName,
                valueParams,
                TIMEOUT
        )
        val json = infoTable.firstRow.getValue("result").toString()
        if (BuildConfig.DEBUG) {
            logger.trace("serviceName: $serviceName, params:\n$params\nresponse:\n$json")
            logger.trace(json)
        }
        return ApiResponse(json)
    }

    /**
     * Выполненяет запрос к ThingWorx
     */
    fun sendFileToThingWorx(fileName: String, thingName: String): Single<FileTransferResult> {
        return Single.fromCallable {
            val client = client!!
            val fts = FileTransferThing(client, thingName)
            try {
                client.bindThing(fts)

                val valueParams = ValueCollection()
                valueParams.put("async", BooleanPrimitive(false))
                valueParams.put("sourceRepo", StringPrimitive(thingName))
                valueParams.put("sourcePath", StringPrimitive("/${FileTransferThing.OUT_FIELD}"))
                valueParams.put("targetRepo", StringPrimitive(BuildConfig.THINGWORX_REPOSITORY))
                valueParams.put("targetPath", StringPrimitive(BuildConfig.THINGWORX_DIRECTORY))
                valueParams.put("sourceFile", StringPrimitive(fileName))
                valueParams.put("targetFile", StringPrimitive(fileName))
                valueParams.put("timeout", NumberPrimitive(FILE_TIMEOUT))

                val res = client.invokeService(
                        RelationshipTypes.ThingworxEntityTypes.Subsystems,
                        "FileTransferSubsystem",
                        "Copy",
                        valueParams,
                        0)
                FileTransferResult.descriptionOf(res.firstRow.getValue("state").toString())
            } finally {
                client.unbindThing(fts)
            }
        }
    }
    /**
     * создание клиента
     */
    private fun createClient(username: String, password: String): AndroidConnectedThingClient {
        val clientConfigurator = ClientConfigurator()
        val securityClaims = SecurityClaims.fromCredentials(username, password)

        clientConfigurator.uri = thingWorxConfigProvider.getConfig().url
        clientConfigurator.reconnectInterval = 15
        clientConfigurator.securityClaims = securityClaims
        clientConfigurator.connectTimeout = 10000
//        clientConfigurator.ignoreSSLErrors(true)

        return AndroidConnectedThingClient(clientConfigurator)
    }

    companion object {
        private const val TIMEOUT = 60000
        private const val FILE_TIMEOUT = 120f
    }
}