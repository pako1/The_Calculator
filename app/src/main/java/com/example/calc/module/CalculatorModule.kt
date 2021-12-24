package com.example.calc.module

import com.example.calc.domain.Calculator
import com.example.calc.domain.CalculatorHelper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
//Lets hilt know that the provided instances will be alive as long as the activity is alive
@InstallIn(ActivityComponent::class)
abstract class CalculatorModule {

    //Binds tells hilt how to provide an instance of the interface (CalculatorHelper) by implementing it on
    // the calculator class
    @Binds
    abstract fun providesCalculatorHelper(calculator: Calculator): CalculatorHelper

}