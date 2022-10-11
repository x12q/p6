package test

import com.qxdzbc.p6.di.P6Module
import dagger.Component

@Component(
    modules = [
        P6Module::class,
    ]
)
interface P6TestComponent {
}
