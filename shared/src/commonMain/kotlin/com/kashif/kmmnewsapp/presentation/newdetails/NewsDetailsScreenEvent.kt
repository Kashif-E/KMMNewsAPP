package com.kashif.kmmnewsapp.presentation.newdetails

import com.kashif.kmmnewsapp.domain.domain_model.HeadlineDomainModel

sealed interface NewsDetailsScreenEvent{

    data class SaveForLater(val headlineDomainModel: HeadlineDomainModel) : NewsDetailsScreenEvent

}