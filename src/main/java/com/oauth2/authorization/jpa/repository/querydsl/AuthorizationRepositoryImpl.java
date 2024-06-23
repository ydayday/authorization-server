package com.oauth2.authorization.jpa.repository.querydsl;


import com.oauth2.authorization.dto.AuthorizationDto;
import com.oauth2.authorization.jpa.entity.Authorization;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Objects;

import static com.oauth2.authorization.jpa.entity.QAuthorization.authorization;
import static com.oauth2.authorization.jpa.entity.QGlobalClientWsInfo.globalClientWsInfo;

@Slf4j
@RequiredArgsConstructor
public class AuthorizationRepositoryImpl implements CustomAuthRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AuthorizationDto.Response> findBySearchCondition(AuthorizationDto.SearchCondition condition, Pageable pageable) {
        List<AuthorizationDto.Response> contents = queryFactory
                .select(
                        Projections.fields(AuthorizationDto.Response.class,
                                authorization.id,
                                authorization.principalName,
                                authorization.accessTokenValue,
                                authorization.accessTokenIssuedAt,
                                authorization.accessTokenExpiresAt,
                                globalClientWsInfo.wsId
                        )
                )
                .from(authorization)
                .where(tokenIssuedAtBetween(condition))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(sorting(pageable))
                .fetch();

        JPAQuery<Authorization> countQuery = queryFactory
                .selectFrom(authorization)
                .where(tokenIssuedAtBetween(condition));

        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchCount);
    }

    private static BooleanExpression tokenIssuedAtBetween(AuthorizationDto.SearchCondition condition) {
        if (Objects.isNull(condition.getStartDate()) && Objects.isNull(condition.getEndDate())) {
            return null;
        }
        return authorization.accessTokenIssuedAt.between(condition.getStartDate(), condition.getEndDate());
    }

    private OrderSpecifier<?> sorting(Pageable page) {
        if (page.getSort().isSorted()) {
            for (Sort.Order order : page.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                if (order.getProperty().equals("id")) {
                    return new OrderSpecifier<>(direction, authorization.id);
                }
                return new OrderSpecifier<>(direction, authorization.accessTokenIssuedAt);
            }
        }

        return new OrderSpecifier<>(Order.DESC, authorization.accessTokenIssuedAt);
    }
}
