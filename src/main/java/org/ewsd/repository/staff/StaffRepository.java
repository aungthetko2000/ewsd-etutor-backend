package org.ewsd.repository.staff;

import org.ewsd.entity.staff.Staff;
import org.ewsd.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffRepository extends JpaRepository<Staff, Long> {

    boolean existsByUser(User user);
}
