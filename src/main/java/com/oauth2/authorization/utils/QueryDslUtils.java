package com.oauth2.authorization.utils;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;

import java.time.LocalDateTime;

public class QueryDslUtils {

    public static StringTemplate formatLocalDateTime(DateTimePath<LocalDateTime> item) {
        return Expressions.stringTemplate("TO_CHAR({0}, {1})",
                item,
                ConstantImpl.create("YYYY-MM-DD HH24:MI:SS"));
    }

}
