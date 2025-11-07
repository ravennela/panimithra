package com.example.fixmate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.fixmate.dtos.request.AddRatingRequest;
import com.example.fixmate.dtos.request.CreateBookingRequest;
import com.example.fixmate.dtos.request.CreateSubCategoryRequest;
import com.example.fixmate.dtos.response.CreateSubcategoryResponse;
import com.example.fixmate.dtos.response.FetchBookingsResponse;
import com.example.fixmate.entities.Bookings;
import com.example.fixmate.entities.ServiceEntity;
import com.example.fixmate.entities.User;
import com.example.fixmate.repositories.BookingRepository;
import com.example.fixmate.repositories.ServiceRepository;
import com.example.fixmate.repositories.UserRepository;

@Service
public class BookingService {
    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ServiceRepository serviceRepository;

    public CreateSubcategoryResponse createBooking(CreateBookingRequest request) {
        boolean isBooked = bookingRepository.existsByCustomer_IdAndService_IdAndBookingDateAndBookingStatusNot(
                request.getUserId(), request.getServiceId(), request.getBookingDate(), request.getBookingStatus());
        if (isBooked) {
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

}
