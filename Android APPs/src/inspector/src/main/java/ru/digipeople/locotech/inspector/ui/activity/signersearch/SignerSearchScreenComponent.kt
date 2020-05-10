package ru.digipeople.locotech.master.ui.activity.searchperformer

import dagger.Subcomponent
import ru.digipeople.locotech.inspector.ui.activity.signersearch.SignerSearchComponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ScreenScope

/**Экранный компонент поиска подписантов
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
@Subcomponent
interface SignerSearchScreenComponent {
    fun  component(module: ActivityModule): SignerSearchComponent
}