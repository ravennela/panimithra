package com.example.fixmate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.fixmate.dtos.request.CreateBookingRequest;
import com.example.fixmate.dtos.response.CreateSubcategoryResponse;
import com.example.fixmate.entities.Bookings;
import com.example.fixmate.entities.ServiceEntity;
import com.example.fixmate.entities.User;
import com.example.fixmate.repositories.BookingRepository;
import com.example.fixmate.repositories.ServiceRepository;
import com.example.fixmate.repositories.UserRepository;
import com.google.firebase.messaging.FirebaseMessagingException;

@Service
public class BookingService {
    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ServiceRepository serviceRepository;
    @Autowired
    NotificationService notificationService;

    public CreateSubcategoryResponse createBooking(CreateBookingRequest request) throws FirebaseMessagingException {
        boolean isBooked = bookingRepository.existsByCustomer_IdAndService_IdAndBookingDateAndBookingStatus(
                request.getUserId(), request.getServiceId(), request.getBookingDate(), "PENDING");
        System.out.println("user id" + request.getUserId() + "serviceid" + request.getServiceId() + "date"
                + request.getBookingDate() + "status" + request.getBookingStatus());
        System.out.println("value of booked" + isBooked);
        if (isBooked) {
            System.out.println("is booked");
            throw new RuntimeException("Booking Already Created In SameDay");
        }
        User user = userRepository.findById(request.getUserId()).orElse(null);
        if (user == null) {
            throw new RuntimeException("User Not Found");
        }
        User employee = userRepository.findById(request.getEmployeeId()).orElse(null);
        if (employee == null) {
            throw new RuntimeException("Employee Not Found");
        }
        ServiceEntity serviceEntity = serviceRepository.findById(request.getServiceId()).orElse(null);
        if (serviceEntity == null) {
            throw new RuntimeException("Service Not Found");
        }
        String employeeToken = employee.getDeviceToken();
        String userToken = user.getDeviceToken();
        Bookings bookings = new Bookings();
        bookings.setBookingDate(request.getBookingDate());
        bookings.setBookingStatus(request.getBookingStatus());
        bookings.setCustomer(user);
        bookings.setEmployee(employee);
        bookings.setDescription(request.getDescription());
        bookings.setName(request.getName());
        bookings.setService(serviceEntity);
        bookings.setTotalAmount(request.getTotalAmount());
        bookingRepository.save(bookings);
        CreateSubcategoryResponse response = new CreateSubcategoryResponse();
        response.setId(bookings.getId());
        System.out.println("token while booking from user side" + userToken);
        if (userToken != null && !userToken.isEmpty()) {
            notificationService.sendNotification(userToken, "Booking Successfull",
                    "Your Booking Is Successful");
        }
        System.out.println("token while booking from employee side" + employeeToken);
        if (employeeToken != null && !employeeToken.isEmpty()) {
            notificationService.sendNotification(employeeToken, "New Booking Arrived",
                    "You Have a New Booking From" + user.getName());
        }
        response.setMessage("Booking Successfully Created");
        return response;

    }

    public Page<Bookings> fetchBookings(String userId, String role, int page, int size) {
        User user = userRepository.findById(userId).orElse(null);
        Page<Bookings> data = null;

        Pageable pageable = PageRequest.of(page, size);
        if (user == null) {
            throw new RuntimeException("User Not Found");
        }
        if (role == "" || role == null) {
            throw new RuntimeException("Role Is Required");
        }
        if ("ADMIN".equals(role)) {
            data = bookingRepository.findAll(pageable);
        }
        if ("USER".equals(role)) {
            data = bookingRepository.findByCustomer_Id(userId, pageable);
        }
        if ("EMPLOYEE".equals(role)) {
            data = bookingRepository.findByEmployee_Id(userId, pageable);
        }
        return data;

    }

    public CreateSubcategoryResponse updateBooking(String status, String bookingId) {

        Bookings bookings = bookingRepository.findById(bookingId).orElse(null);
        if (bookings == null) {
            throw new RuntimeException("No Bookings Found with this Id");
        }
        bookings.setBookingStatus(status);
        bookingRepository.save(bookings);
        CreateSubcategoryResponse response = new CreateSubcategoryResponse();
        response.setMessage("Booking Successfully Updated");
        response.setId(bookingId);
        return response;
    }

    public Bookings getBookingById(String bookingId) {

        Bookings bookings = bookingRepository.findById(bookingId).orElse(null);
        if (bookings == null) {
            throw new RuntimeException("No Bookings Found With this Id");
        }
        return bookings;
    }

    public CreateSubcategoryResponse updatePaymentStatus(String bookingId) {
        Bookings bookings = bookingRepository.findById(bookingId).orElse(null);
        if (bookings == null) {
            throw new RuntimeException("No Bookings Found With this Id");
        }
        bookings.setPaymentStatus("PAID");
        bookingRepository.save(bookings);
        CreateSubcategoryResponse response = new CreateSubcategoryResponse();
        response.setId(bookingId);
        response.setMessage("Payment Status Updated Successfully");
        return response;
    }

}
