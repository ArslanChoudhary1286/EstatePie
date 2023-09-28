package com.jeuxdevelopers.estatepie.network.responses.plans

data class SubscriptionPlansResponse(
    val `data`: Data,
    val message: String,
    val success: Boolean
){
    data class Data(
        val free: Free,
        val premium: Premium,
        val standard: Standard
    ){
        data class Free(
            val billingDayOfMonth: String,
            val billingFrequency: Int,
            val currencyIsoCode: String,
            val description: String,
            val monthly: Monthly,
            val name: String,
            val numberOfBillingCycles: String,
            val subtitle: String,
            val trialDuration: String,
            val trialDurationUnit: String,
            val trialPeriod: String,
            val yearly: Yearly
        ){

            data class Monthly(
                val id: String,
                val merchantId: String,
                val name: String,
                val plan_id: Int,
                val price: String
            )

            data class Yearly(
                val id: String,
                val merchantId: String,
                val name: String,
                val plan_id: Int,
                val price: String
            )
        }

        data class Premium(
            val `data`: Data,
            val monthly: Monthly,
            val yearly: Yearly
        ){

            data class Data(
                val billingDayOfMonth: String,
                val billingFrequency: Int,
                val currencyIsoCode: String,
                val description: String,
                val name: String,
                val numberOfBillingCycles: String,
                val subtitle: String,
                val trialDuration: String,
                val trialDurationUnit: String,
                val trialPeriod: String
            )

            data class Monthly(
                val id: String,
                val merchantId: String,
                val name: String,
                val plan_id: Int,
                val price: String
            )

            data class Yearly(
                val id: String,
                val merchantId: String,
                val name: String,
                val plan_id: Int,
                val price: String
            )
        }

        data class Standard(
            val `data`: Data,
            val monthly: Monthly,
            val yearly: Yearly
        ){

            data class Data(
                val billingDayOfMonth: String,
                val billingFrequency: Int,
                val currencyIsoCode: String,
                val description: String,
                val name: String,
                val numberOfBillingCycles: String,
                val subtitle: String,
                val trialDuration: String,
                val trialDurationUnit: String,
                val trialPeriod: String
            )

            data class Monthly(
                val id: String,
                val merchantId: String,
                val name: String,
                val plan_id: Int,
                val price: String
            )

            data class Yearly(
                val id: String,
                val merchantId: String,
                val name: String,
                val plan_id: Int,
                val price: String
            )
        }
    }
}