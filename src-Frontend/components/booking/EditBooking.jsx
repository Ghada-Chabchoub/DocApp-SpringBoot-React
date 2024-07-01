import React, { useEffect } from "react"
import moment from "moment"
import { useState } from "react"
import { Form, FormControl, Button } from "react-bootstrap"
import { updateBooking,getBookingsByUserEmail } from "../utils/ApiFunctions"
import { useNavigate, useParams } from "react-router-dom"

const EditBooking = () => {
	const [validated, setValidated] = useState(false)
	const [isSubmitted, setIsSubmitted] = useState(false)
	const [availableTimes, setAvailableTimes] = useState([]);
	
	const [successMessage, setSuccessMessage] = useState("")
	const [errorMessage, setErrorMessage] = useState("")
	const currentUser = localStorage.getItem("userEmail")

	const [booking, setBooking] = useState({
		id:"",
		patientEmail: currentUser,
		date: "",
		heur: ""
		
	})
	

	const { bookingId } = useParams()
	const navigate = useNavigate()
	const userEmail = localStorage.getItem("userEmail")

   
	
	useEffect(() => {
		generateAvailableTimes();
	  }, []);

	const handleInputChange = (e) => {
		const { name, value } = e.target
		setBooking({ ...booking, [name]: value })
		
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
	
	 

	useEffect(() => {
		const fetchBookings = async () => {
			try {
				const response = await getBookingsByUserEmail(userEmail)
				setBooking(response)
			} catch (error) {
				console.error("Error fetching bookings:", error.message)
				setErrorMessage(error.message)
			}
		}

		fetchBookings()
	}, [userEmail])

	const handleSubmit = async (e) => {
		e.preventDefault();
		const form = e.currentTarget;
		if (form.checkValidity() === false || !availableTimes) {
			e.stopPropagation();
		} else {
			try {
				const response = await updateBooking(bookingId, booking);
				if (response.status === 200){
					//setIsSubmitted(true);
					setSuccessMessage("booking updated successfully!")
					const updatedBookingData = await getBookingsByUserEmail(userEmail);
					setBooking(updatedBookingData);
					setErrorMessage("")

				}else {
					setErrorMessage("Error updating booking")
				}
			} catch (error) {
			console.error(error)
			setErrorMessage(error.message)
			}
	}
		//setValidated(true);
	};
	
	
			

   
	return (
		<>
        
		<div className="container mb-5">
				<div className="row">
					<div className="col-md-6">
						<div className="card card-body mt-5">
					{successMessage && (
						<div className="alert alert-success" role="alert">
							{successMessage}
						</div>
					)}
					{errorMessage && (
						<div className="alert alert-danger" role="alert">
							{errorMessage}
						</div>
					)}
				
					
						
							<h4 className="card-title">Update your Appointment </h4>

							<Form noValidate validated={validated} onSubmit={handleSubmit}>
								

								<Form.Group>
									<Form.Label htmlFor="patientEmail" className="hotel-color">
										Email
									</Form.Label>
									<FormControl
										
										type="email"
										id="patientEmail"
										name="patientEmail"
                                        value={booking.patientEmail}
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
												
												type="date"
												id="date"
												name="date"
												value={booking.date}
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
										<select 
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
										Update
									</button>
								</div>
							</Form>
						</div>
					</div>

					
				</div>
			</div>
            
		</>
	)
}
export default EditBooking
