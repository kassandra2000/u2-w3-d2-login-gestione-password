package kassandrafalsitta.u2w3d1.controllers;

import jakarta.validation.Valid;
import kassandrafalsitta.u2w3d1.entities.Employee;
import kassandrafalsitta.u2w3d1.entities.Reservation;
import kassandrafalsitta.u2w3d1.entities.Travel;
import kassandrafalsitta.u2w3d1.exceptions.BadRequestException;
import kassandrafalsitta.u2w3d1.exceptions.NotFoundException;
import kassandrafalsitta.u2w3d1.payloads.EmployeeDTO;
import kassandrafalsitta.u2w3d1.payloads.EmployeeRespDTO;
import kassandrafalsitta.u2w3d1.payloads.ReservationDTO;
import kassandrafalsitta.u2w3d1.services.EmployeesService;
import kassandrafalsitta.u2w3d1.services.ReservationsService;
import kassandrafalsitta.u2w3d1.services.TravelsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.context.support.DefaultMessageSourceResolvable;


import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employees")
public class EmployeesController {
    @Autowired
    private EmployeesService employeesService;
    @Autowired
    private ReservationsService reservationsService;
  @Autowired
    private TravelsService travelsService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Employee> getAllEmployees(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size,
                                        @RequestParam(defaultValue = "id") String sortBy) {
        return this.employeesService.findAll(page, size, sortBy);
    }

    @GetMapping("/{employeeId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Employee getEmployeeById(@PathVariable UUID employeeId) {
        return employeesService.findById(employeeId);
    }

    @PutMapping("/{employeeId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Employee findEmployeeByIdAndUpdate(@PathVariable UUID employeeId, @RequestBody @Validated EmployeeDTO body) {
        return employeesService.findByIdAndUpdate(employeeId, body);
    }

    @DeleteMapping("/{employeeId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findEmployeeByIdAndDelete(@PathVariable UUID employeeId) {
        employeesService.findByIdAndDelete(employeeId);
    }

    @PostMapping("/{employeeId}/avatar")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Employee uploadCover(@PathVariable UUID employeeId, @RequestParam("avatar") MultipartFile image) throws IOException {
        return  this.employeesService.uploadImage(employeeId,image);
    }

    // me
    @GetMapping("/me")
    public Employee getProfile(@AuthenticationPrincipal Employee currentAuthenticatedEmployee) {
        return currentAuthenticatedEmployee;
    }

    @PutMapping("/me")
    public Employee updateProfile(@AuthenticationPrincipal Employee currentAuthenticatedEmployee, @RequestBody EmployeeDTO body) {
        return this.employeesService.findByIdAndUpdate(currentAuthenticatedEmployee.getId(), body);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(@AuthenticationPrincipal Employee currentAuthenticatedEmployee) {
        this.employeesService.findByIdAndDelete(currentAuthenticatedEmployee.getId());
    }
    @PostMapping("/me/avatar")
    public Employee uploadProfileCover(@AuthenticationPrincipal Employee currentAuthenticatedEmployee, @RequestParam("avatar") MultipartFile image) throws IOException {
        return  this.employeesService.uploadImage(currentAuthenticatedEmployee.getId(),image);
    }

    //  me/reservation
    @GetMapping("/me/reservation")
    public List<Reservation> getProfileReservation(@AuthenticationPrincipal Employee currentAuthenticatedEmployee) {
        return this.reservationsService.findByEmployee(currentAuthenticatedEmployee);
    }

    @GetMapping("/me/reservation/{reservationId}")
    public Reservation getProfileReservationById(@AuthenticationPrincipal Employee currentAuthenticatedEmployee,@PathVariable UUID reservationId) {
        List<Reservation> reservationList = this.reservationsService.findByEmployee(currentAuthenticatedEmployee);
        Reservation employeeReservation = reservationList.stream().filter(reservation -> reservation.getId().equals(reservationId)).findFirst()
                .orElseThrow(() -> new NotFoundException(reservationId));
        return reservationsService.findById(employeeReservation.getId());
    }

    //non funziona
    @PutMapping("/me/reservation/{reservationId}")
    public Reservation updateProfileReservation(@AuthenticationPrincipal Employee currentAuthenticatedEmployee, @PathVariable UUID reservationId, @RequestBody ReservationDTO body) {
        List<Reservation> reservationList = this.reservationsService.findByEmployee(currentAuthenticatedEmployee);
        Reservation employeeReservation = reservationList.stream().filter(reservation -> reservation.getId().equals(reservationId)).findFirst()
                .orElseThrow(() -> new NotFoundException(reservationId));
        return this.reservationsService.findByIdAndUpdate(employeeReservation.getId(), body);
    }
    //non funziona
    @DeleteMapping("/me/reservation/{reservationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfileReservation(@AuthenticationPrincipal Employee currentAuthenticatedEmployee, @PathVariable UUID reservationId) {
        List<Reservation> reservationList = this.reservationsService.findByEmployee(currentAuthenticatedEmployee);
        Reservation employeeReservation = reservationList.stream().filter(reservation -> reservation.getId().equals(reservationId)).findFirst() // Get the first matching reservation
                .orElseThrow(() -> new NotFoundException(reservationId));
        this.reservationsService.findByIdAndDelete(employeeReservation.getId());
    }

}
