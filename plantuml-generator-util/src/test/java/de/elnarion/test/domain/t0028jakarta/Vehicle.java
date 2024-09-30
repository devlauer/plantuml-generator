package de.elnarion.test.domain.t0028jakarta;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class Vehicle {

	@Size(min=2, max=6)
	private final List<Wheel> wheels = new ArrayList<>();

	private final List<Seat> seats = new ArrayList<>();

	@NotEmpty
	private final List<Light> lights = new ArrayList<>();

	public List<Wheel> getWheels() {
		return wheels;
	}

	@Size(min=1, max=10)
	public List<Seat> getSeats() {
		return seats;
	}

	public List<Light> getLights() {
		return lights;
	}
}
