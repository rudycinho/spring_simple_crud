package com.rudycinho.springsimplecrud.resources;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rudycinho.springsimplecrud.models.dto.ReservationDTO;
import com.rudycinho.springsimplecrud.models.pojo.Reservation;
import com.rudycinho.springsimplecrud.models.vo.ReservationVO;
import com.rudycinho.springsimplecrud.services.ReservationService;

@RestController
@RequestMapping("/api/reservations")
public class ReservationResource {
	
	@Autowired
	private ReservationService reservationService;
	
	@PostMapping
	public ResponseEntity<?> create(@RequestBody ReservationVO reservationVO){
		Reservation reservation = new Reservation(reservationVO);
		reservation = reservationService.create(reservation);
		ReservationDTO reservationDTO = new ReservationDTO(reservation);
		return new ResponseEntity<>(reservationDTO,HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody ReservationVO reservationVO){
		ResponseEntity<?> response;
		Reservation reservation = reservationService.get(id);
		if(reservation==null) {
			Map<String,String> errors = new HashMap<>();
			errors.put("Errors", "The reservation does not exist");
			response = new ResponseEntity<>(errors,HttpStatus.NOT_FOUND);
		}else {
			reservation.setReservation(reservationVO);
			reservation = reservationService.update(reservation);
			ReservationDTO reservationDTO = new ReservationDTO(reservation);
			response = new ResponseEntity<>(reservationDTO,HttpStatus.CREATED);
		}
		return response;
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id")int id){
		ResponseEntity<?> response;
		Reservation reservation = reservationService.get(id);
		if(reservation==null) {
			Map<String,String> errors = new HashMap<>();
			errors.put("Errors", "The reservation does not exist");
			response = new ResponseEntity<>(errors,HttpStatus.NOT_FOUND);
		}else {
			reservationService.delete(reservation);
			ReservationDTO reservationDTO = new ReservationDTO(reservation);
			response = new ResponseEntity<>(reservationDTO,HttpStatus.OK);
		}
		return response;
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> get(@PathVariable("id")int id){
		ResponseEntity<?> response;
		Reservation reservation = reservationService.get(id);
		if(reservation==null) {
			Map<String,String> errors = new HashMap<>();
			errors.put("Errors", "The reservation does not exist");
			response = new ResponseEntity<>(errors,HttpStatus.NOT_FOUND);
		}else {
			ReservationDTO reservationDTO = new ReservationDTO(reservation);
			response = new ResponseEntity<>(reservationDTO,HttpStatus.OK);
		}
		return response;
	}
	
	@GetMapping
	public ResponseEntity<?> get(){
		ResponseEntity<?> response;
		List<Reservation> reservations = reservationService.getAll();
		if(reservations.size()==0) {
			Map<String,String> errors = new HashMap<>();
			errors.put("Errors", "No reservations");
			response = new ResponseEntity<>(errors,HttpStatus.NOT_FOUND);
		}else {
			List<ReservationDTO> reservationsDTO = new LinkedList<>();
			ReservationDTO reservationDTO;
			for(Reservation reservation : reservations) {
				reservationDTO = new ReservationDTO(reservation);
				reservationsDTO.add(reservationDTO);
			}
			response = new ResponseEntity<>(reservationsDTO,HttpStatus.OK);
		}
		return response;
	}
}