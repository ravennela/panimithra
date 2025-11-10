package com.example.fixmate.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.fixmate.dtos.response.EmployeeDashboardResponse;
import com.example.fixmate.dtos.response.MonthWiseRevenue;
import com.example.fixmate.entities.User;
import com.example.fixmate.repositories.BookingRepository;
import com.example.fixmate.repositories.UserRepository;

@Service
public class EmployeeDashboardService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    BookingRepository bookingRepository;

    public EmployeeDashboardResponse getDashboardResponse(String userId) {
        User employee = userRepository.findById(userId).orElse(null);
        if (employee == null) {
            throw new RuntimeException("No User Found");
        }
        LocalDate now = LocalDate.now();

        long totalBookings = bookingRepository.countOfBookingByUserId(userId);

        long inprogressCount = bookingRepository.employeeInprogessBookingCount(userId, "INPROGRESS");

        long pendingCount = bookingRepository.employeeInprogessBookingCount(userId, "PENDING");
        long completedBookings = bookingRepository.employeeInprogessBookingCount(userId, "COMPLETED");

        long rejectedCount = bookingRepository.employeeInprogessBookingCount(userId, "REJECTED");
        long cancalledBookings = bookingRepository.employeeInprogessBookingCount(userId, "CANCELLED");

        Double currentMonthRevenue = bookingRepository.employeeCurrentMonthRevenue(userId, now.getMonthValue(),
                now.getYear());
        double revenue = (currentMonthRevenue != null) ? currentMonthRevenue : 0.0;
        List<MonthWiseRevenue> monthData = bookingRepository.getEmployeeMonthlyRevenue(userId, now.getYear());

        EmployeeDashboardResponse response = new EmployeeDashboardResponse();

        response.setBookingsCompleted(completedBookings);
        response.setEmployeeName(employee.getName());
        response.setBookingsInprogress(inprogressCount + pendingCount);
        response.setMonthWiseRevenue(monthData);
        response.setRevenue(revenue);
        response.setRejectedBookings(rejectedCount);
        response.setCancelledBookings(cancalledBookings);
        response.setTotalBookings(totalBookings);
        return response;
    }

}
