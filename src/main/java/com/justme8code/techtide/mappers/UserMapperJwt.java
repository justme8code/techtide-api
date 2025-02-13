package com.justme8code.techtide.mappers;

import com.justme8code.techtide.dtos.UserDtoJwt;
import com.justme8code.techtide.models.User;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapperJwt {
    User toEntity(UserDtoJwt userDtoJwt);

    UserDtoJwt toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserDtoJwt userDtoJwt, @MappingTarget User user);
}