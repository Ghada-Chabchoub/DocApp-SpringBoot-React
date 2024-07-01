import React, { useEffect } from "react"
import moment from "moment"
import { useState } from "react"
import { Form, FormControl, Button } from "react-bootstrap"
import BookingSummary from "./BookingSummary"
import { bookApp } from "../utils/ApiFunctions"
import { useNavigate, useParams } from "react-router-dom"
import { useAuth } from "../auth/AuthProvider"

const BookingForm = () => {
	const [validated, setValidated] = useState(false)
	const [isSubmitted, setIsSubmitted] = useState(false)
	const [errorMessage, setErrorMessage] = useState("")
	const [availableTimes, setAvailableTimes] = useState([]);
	

  const currentUser = localStorage.getItem("userEmail")

	const [booking, setBooking] = useState({
		patientFullName: "",
		patientEmail: currentUser,
		date: "",
		heur: ""
		
	})
	

	const { doctorId } = useParams()
	const navigate = useNavigate()

	
	useEffect(() => {
		generateAvailableTimes();
	  }, []);

	const handleInputChange = (e) => {
		const { name, value } = e.target
		setBooking({ ...booking, [name]: value })
		setErrorMessage("")
	}

	  const generateAvailableTimes = () => {
		const startTime = moment().hour(8).minute(0); // 8:00 AM
		const endTime = moment().hour(17).minute(0); // 5:00 PM
		const times = [];
	  
		if (startTime.isValid() && endTime.isValid()) {
			while (startTime.isBefore(endTime)) {
				times.push(startTime.format("HH:mm"));
				startTime.add(15, "minutes"); // Add 15 minutes
			}
			setAvailableTimes(times);
		} else {
		
			console.error("Invalid start or end time.");
		}
	};
	
	 
    

	  const handleSubmit = (e) => {
		e.preventDefault();
		const form = e.currentTarget;// Récupère le formulaire actuel
		if (form.checkValidity() === false || !availableTimes) {
			e.stopPropagation()
		 
		}
		else{
			setIsSubmitted(true)

		}
		setValidated(true)
	  };


	const handleBooking = async () => {
		try {
			const confirmationCode = await bookApp(doctorId, booking)
			setIsSubmitted(true)
			navigate("/booking-success", { state: { message: confirmationCode } })
		} catch (error) {
			const errorMessage = error.message
			navigate("/booking-success", { state: {error: errorMessage } })

		}
	}

	return (
		<>
			<div className="container mb-5">
				<div className="row">
					<div className="col-md-6">
						<div className="card card-body mt-5">
							<h4 className="card-title">Reserve An Appointment </h4>

							<Form noValidate validated={validated} onSubmit={handleSubmit}>
								<Form.Group>
									<Form.Label htmlFor="patientFullName" className="hotel-color">
										FullName
									</Form.Label>
									<FormControl
										required
										type="text"
										id="patientFullName"
										name="patientFullName"
										value={booking.patientFullName}
										placeholder="Enter your fullname"
										onChange={handleInputChange}
									/>
									<Form.Control.Feedback type="invalid">
										Please enter your fullname.
									</Form.Control.Feedback>
								</Form.Group>

								<Form.Group>
									<Form.Label htmlFor="patientEmail" className="hotel-color">
										Email
									</Form.Label>
									<FormControl
										required
										type="email"
										id="patientEmail"
										name="patientEmail"
										value={booking.patientEmail}
										placeholder="Enter your email"
										onChange={handleInputChange}
										disabled
									/>
									<Form.Control.Feedback type="invalid">
										Please enter a valid email address.
									</Form.Control.Feedback>
								</Form.Group>

								<fieldset style={{ border: "2px" }}>
									<legend>Date</legend>
									<div className="row">
										<div className="col-6">
											<Form.Label htmlFor="date" className="hotel-color">
												Appointment date
											</Form.Label>
											<FormControl
												required
												type="date"
												id="date"
												name="date"
												value={booking.date}
												placeholder="Appointment-date"
												min={moment().format("MMM Do, YYYY")}
												onChange={handleInputChange}
											/>
											<Form.Control.Feedback type="invalid">
												Please select a date.
											</Form.Control.Feedback>
										</div>

										<Form.Label htmlFor="heur" className="hotel-color">
                                             Appointment hour
                                        </Form.Label>
										<select required
                                             id="heur"
                                             name="heur"
                                             value={booking.heur}
                                             onChange={handleInputChange}
                                    >
                                              {availableTimes.map((time) => (
                                            <option key={time} value={time}>
                                              {time}
                                            </option>
                                          ))}
                                       </select>

                                        <Form.Control.Feedback type="invalid">
                                           Please select an hour between 8:00 AM and 5:00 PM
                                        </Form.Control.Feedback>

										{errorMessage && <p className="error-message text-danger">{errorMessage}</p>}
									</div>
								</fieldset>

                                   <div className="fom-group mt-2 mb-2">
									<button type="submit" className="btn btn-hotel">
										Continue
									</button>
								</div>
							</Form>
						</div>
					</div>

					<div className="col-md-4">
						{isSubmitted && (
							<BookingSummary
								booking={booking}
								onConfirm={handleBooking}
								isFormValid={validated}
							/>
						)}
					</div>
				</div>
			</div>
		</>
	)
}
export default BookingForm