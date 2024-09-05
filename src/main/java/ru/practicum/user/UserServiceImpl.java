package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.user.dto.UserCreateDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.mapper.UserMapper;
import ru.practicum.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::userToUserDto)
                .toList();
    }

    @Override
    public UserDto saveUser(UserCreateDto userCreateDto) {
        User user = userRepository.save(userMapper.userCreateDtoToUser(userCreateDto));
        return userMapper.userToUserDto(user);
    }
}