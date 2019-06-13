package com.helpfulapps.data.db.extensions

import com.raizlabs.android.dbflow.sql.language.SQLOperator
import com.raizlabs.android.dbflow.sql.language.property.Property
import com.raizlabs.android.dbflow.sql.language.property.PropertyFactory


infix fun <T : Any?> Property<T>.between(time: T): SQLOperator =
    PropertyFactory.from(this).between(this - time).and(this + time)