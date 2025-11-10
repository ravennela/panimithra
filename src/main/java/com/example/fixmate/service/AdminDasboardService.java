package com.example.fixmate.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.fixmate.dtos.response.AdminDashboardResponse;
import com.example.fixmate.dtos.response.CityBookingCountDTO;
import com.example.fixmate.dtos.response.CityEmployeeCountDTO;
import com.example.fixmate.repositories.BookingRepository;
import com.example.fixmate.repositories.UserRepository;

@Service
public class AdminDasboardService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    BookingRepository bookingRepository;

    public AdminDashboardResponse getDashboardData() {
        LocalDate now = LocalDate.now();
        LocalDate previousMonth = now.minusMonths(1);
        long currentMonthUserCount = userRepository.countUsersRegisteredInMonthByRole(
                now.getMonthValue(),
                now.getYear(),
                "USER");

        long previousMonthUserCount = userRepository.countUsersRegisteredInMonthByRole(
                previousMonth.getMonthValue(),
                previousMonth.getYear(),
                "USER");

        long currentMonthEmployeeCount = userRepository.countUsersRegisteredInMonthByRole(
                now.getMonthValue(),
                now.getYear(),
                "EMPLOYEE");

        long previousMonthEmployeeCount = userRepository.countUsersRegisteredInMonthByRole(
                previousMonth.getMonthValue(),
                previousMonth.getYear(),
                "EMPLOYEE");

        long completedBookingsByThisMonth = bookingRepository.pendingBoookingsInMonth(now.getMonthValue(),
                now.getYear(), "COMPLETED");
        long cancelledBookingsByThisMonth = bookingRepository.pendingBoookingsInMonth(now.getMonthValue(),
                now.getYear(), "CANCELLED");
        long completedBookingsByPreviouMonth = bookingRepository.pendingBoookingsInMonth(previousMonth.getMonthValue(),
                previousMonth.getYear(), "COMPLETED");
        long pendingdBookingsByPreviouMonth = bookingRepository.pendingBoookingsInMonth(previousMonth.getMonthValue(),
                previousMonth.getYear(), "PENDING");
        long inprogressBookingsByPreviouMonth = bookingRepository.pendingBoookingsInMonth(previousMonth.getMonthValue(),
                previousMonth.getYear(), "INPROGRESS");

        long pendingBookingsByThisMonth = bookingRepository.pendingBoookingsInMonth(now.getMonthValue(),
                now.getYear(), "PENDING");

        long inprogressBookingsByThisMonth = bookingRepository.pendingBoookingsInMonth(now.getMonthValue(),
                now.getYear(), "INPROGRESS");
        long rejectedBookingsByThisMonth = bookingRepository.pendingBoookingsInMonth(now.getMonthValue(),
                now.getYear(), "REJECTED");
        Double revenueValue = bookingRepository.revenueByTheCurrentMonth(now.getMonthValue(), now.getYear(),
                "COMPLETED");
        double revenue = (revenueValue != null) ? revenueValue : 0.0;

        List<CityBookingCountDTO> cityCount = bookingRepository.overAllBookingsByCity();

        List<CityEmployeeCountDTO> employeeCount = userRepository.findEmployeeCountGroupedByCity("EMPLOYEE");
        long totalBookings = bookingRepository.findBookingCount();
        long totalBookingsCompleted = bookingRepository.countBookingsByStatus("COMPLETED");
        long totalBookingsPending = bookingRepository.countBookingsByStatus("PENDING");
        long totalBookingsCancelled = bookingRepository.countBookingsByStatus("CANCELLED");
        long totalBookinsInprogress = bookingRepository.countBookingsByStatus("INPOROGRESS");
        long totalBookinsRejected = bookingRepository.countBookingsByStatus("REJECTED");

        AdminDashboardResponse adminDashboardResponse = new AdminDashboardResponse();
        adminDashboardResponse.setCancelledBookings(cancelledBookingsByThisMonth);
        adminDashboardResponse.setCityBookings(cityCount);
        adminDashboardResponse.setCityEmployee(employeeCount);
        adminDashboardResponse.setCompletedBookings(completedBookingsByThisMonth);
        adminDashboardResponse.setCurrentMonthEmployees(currentMonthEmployeeCount);
        adminDashboardResponse.setCurrentMonthUsers(currentMonthUserCount);
        adminDashboardResponse.setInporgressBookings(inprogressBookingsByThisMonth);
        adminDashboardResponse.setPendingBookings(pendingBookingsByThisMonth);
        adminDashboardResponse.setPreviousMonthEmployees(previousMonthEmployeeCount);
        adminDashboardResponse.setPreviousMonthuser(previousMonthUserCount);
        adminDashboardResponse.setRejectedBookings(rejectedBookingsByThisMonth);
        adminDashboardResponse.setRevenue(revenue);
        adminDashboardResponse.setTotalBookingsRejected(totalBookinsRejected);
        adminDashboardResponse.setTotalBookinsCancelled(totalBookingsCancelled);
        adminDashboardResponse.setPendingdBookingsByPreviouMonth(pendingdBookingsByPreviouMonth);
        adminDashboardResponse.setCompletedBookingsByPreviousMonth(completedBookingsByPreviouMonth);

        adminDashboardResponse.setTotalBookings(totalBookings);
        adminDashboardResponse.setTotalBookingsCompleted(totalBookingsCompleted);
        adminDashboardResponse.setTotalBookingsPending(totalBookingsPending + totalBookinsInprogress);
        return adminDashboardResponse;
    }
}
