package com.community.demo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAuthUser is a Querydsl query type for AuthUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAuthUser extends EntityPathBase<AuthUser> {

    private static final long serialVersionUID = 56931397L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAuthUser authUser = new QAuthUser("authUser");

    public final EnumPath<AuthRole> auth = createEnum("auth", AuthRole.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QUser user;

    public QAuthUser(String variable) {
        this(AuthUser.class, forVariable(variable), INITS);
    }

    public QAuthUser(Path<? extends AuthUser> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAuthUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAuthUser(PathMetadata metadata, PathInits inits) {
        this(AuthUser.class, metadata, inits);
    }

    public QAuthUser(Class<? extends AuthUser> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

