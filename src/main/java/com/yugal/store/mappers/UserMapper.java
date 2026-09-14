package com.yugal.store.mappers;

import com.yugal.store.dtos.RegisterUserRequest;
import com.yugal.store.dtos.UpdateUserRequest;
import com.yugal.store.dtos.UserDto;
import com.yugal.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
//    @Mapping( target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    UserDto toDto(User user);
    User toEntity(RegisterUserRequest request);
    User update(UpdateUserRequest request, @MappingTarget User user);
}
