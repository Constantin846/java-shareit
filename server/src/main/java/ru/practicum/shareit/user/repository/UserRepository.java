package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.User;

import java.util.List;

@Deprecated
public interface UserRepository {
    User create(User user);

    List<User> findAll();

    User findById(long userId);

    User update(User user);

    void delete(long userId);

    boolean checkUserExists(long userId);
}
