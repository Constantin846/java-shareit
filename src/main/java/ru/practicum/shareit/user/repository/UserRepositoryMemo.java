package ru.practicum.shareit.user.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepositoryMemo implements UserRepository {
    private final Map<Long, User> users;
    private final Map<String, User> emailOfUsers;

    @Override
    public User create(User user) {
        if (emailOfUsers.containsKey(user.getEmail())) {
            String message = String.format("This email has been occupied: %s", user);
            log.warn(message);
            throw new ConflictException(message);
        }
        user.setId(generateId());
        users.put(user.getId(), user);
        emailOfUsers.put(user.getEmail(), user);
        return user;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User findById(long userId) {
        if (users.containsKey(userId)) {
            return users.get(userId);
        } else {
            String message = String.format("An user was not found by id: %d", userId);
            log.warn(message);
            throw new NotFoundException(message);
        }
    }

    @Override
    public User update(User user) {
        if (user.getId() == null) {
            String message = String.format("The user's id is null: %s", user);
            log.warn(message);
            throw new ValidationException(message);
        }

        if (users.containsKey(user.getId())) {
            if (user.getEmail() != null && emailOfUsers.containsKey(user.getEmail())) {
                String message = String.format("This email has been occupied: %s", user);
                log.warn(message);
                throw new ConflictException(message);
            }

            User oldUser = users.get(user.getId());

            if (user.getEmail() != null) {
                emailOfUsers.remove(oldUser.getEmail());
                oldUser.setEmail(user.getEmail());
                emailOfUsers.put(oldUser.getEmail(), oldUser);
            }
            if (user.getName() != null) {
                oldUser.setName(user.getName());
            }
            return oldUser;

        } else {
            String message = String.format("An user was not found by id: %s", user);
            log.warn(message);
            throw new NotFoundException(message);
        }
    }

    @Override
    public void delete(long userId) {
        if (users.containsKey(userId)) {
            users.remove(userId);
        } else {
            String message = String.format("An user was not found by id: %d", userId);
            log.warn(message);
            throw new NotFoundException(message);
        }
    }

    @Override
    public boolean checkUserExists(long userId) {
        return users.containsKey(userId);
    }

    private long sequenceId = 0;

    private long generateId() {
        return ++sequenceId;
    }
}
