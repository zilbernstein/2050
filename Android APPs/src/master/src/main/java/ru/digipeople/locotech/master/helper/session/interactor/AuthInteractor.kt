package ru.digipeople.locotech.master.helper.session.interactor

import ru.digipeople.locotech.core.data.api.CoreThingsWorxWorker
import ru.digipeople.locotech.core.data.api.response.SignInResponse
import ru.digipeople.locotech.core.helper.session.interactor.BaseAuthInteractor
import ru.digipeople.locotech.core.push.service.FbcTokenManager
import ru.digipeople.locotech.master.helper.session.mapper.SignInInfoMapper
import ru.digipeople.locotech.master.helper.session.model.SignInInfo
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import javax.inject.Inject

/**
 * Обрабатывает метод авторизации
 */
class AuthInteractor @Inject constructor(
        thingsWorxWorker: CoreThingsWorxWorker,
        errorBuilder: SimpleApiUserErrorBuilder,
        fbcTokenManager: FbcTokenManager
) : BaseAuthInteractor<SignInInfo>(thingsWorxWorker, errorBuilder, fbcTokenManager) {
    override fun mapAuthResponseToAuthInfo(signInResponse: SignInResponse) =
            SignInInfoMapper.get().fromSignInResponse(signInResponse)
}