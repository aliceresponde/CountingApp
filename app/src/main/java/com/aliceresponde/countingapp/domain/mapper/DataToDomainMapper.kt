package com.aliceresponde.countingapp.domain.mapper

import com.aliceresponde.countingapp.data.local.CounterEntity
import com.aliceresponde.countingapp.domain.model.Counter

interface DataToDomainMapper {
    fun dataToDomain(entity: CounterEntity): Counter
}

class DataToDomainMapperImp : DataToDomainMapper {
    override fun dataToDomain(entity: CounterEntity): Counter {
        return Counter(id = entity.id, title = entity.title, count = entity.count)
    }
}


