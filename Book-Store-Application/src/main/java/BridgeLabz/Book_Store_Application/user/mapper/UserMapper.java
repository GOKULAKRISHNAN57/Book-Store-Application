package BridgeLabz.Book_Store_Application.user.mapper;


import BridgeLabz.Book_Store_Application.user.dto.UpdateProfileRequest;
import BridgeLabz.Book_Store_Application.user.dto.UserResponse;
import BridgeLabz.Book_Store_Application.user.dto.UserSummaryResponse;
import BridgeLabz.Book_Store_Application.user.entity.User;
import org.modelmapper.ModelMapper;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserResponse toResponse(User user) {
        return modelMapper.map(user, UserResponse.class);
    }

    public UserSummaryResponse toSummary(User user) {
        return modelMapper.map(user, UserSummaryResponse.class);
    }

    public void updateProfile(UpdateProfileRequest request, User user) {
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
    }

}