package ru.digipeople.locotech.inspector.ui.activity.signersearch

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import ru.digipeople.locotech.inspector.model.Signer
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Класс для хранения информации о подписантах при переходе на другой экран
 *
 * @author Aleksandr Brazhkin
 */
@Singleton
class SingerSearchResultStorage @Inject constructor() {

    private var _data: Set<Signer>? = null

    private val subject = PublishSubject.create<Set<Signer>>()
    /**
     * получить данные
     */
    fun getData(): Set<Signer>? {
        return _data
    }

    fun dataChanges(): Observable<Set<Signer>> = subject
    /**
     * положить данные
     */
    fun putData(data: Set<Signer>) {
        _data = data
        subject.onNext(data)
    }

    fun clearData() {
        _data = null
    }
}