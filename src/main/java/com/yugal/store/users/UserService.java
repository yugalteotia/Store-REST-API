package com.yugal.store.users;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public List<UserDto> getAllUsers(String sortBy) {
        sortBy = Set.of("name", "email").contains(sortBy) ? sortBy : "name";
        return userRepository.findAll(Sort.by(sortBy)).stream().map(userMapper::toDto).toList();
    }

    public User getUserById(Long id) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) throw new UserNotFoundException("User not found with id: " + id);
        return user;
    }

    public User createUser(@Valid RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new DuplicateUserException("Email is already registered: " + request.getEmail());

        var user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        return userRepository.save(user);
    }

    public User updateExistingUser(Long id, UpdateUserRequest request) {
        var user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return userRepository.save(userMapper.update(request, user));
    }

    public void deleteUser(Long id) {
        var user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
    }

    public void updatePassword(Long id, ChangePasswordRequest request) {
        var user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword()))
            throw new BadCredentialsException("Old password is incorrect for user with id: " + id);

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
