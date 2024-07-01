import React, { useState } from "react"
import moment from "moment"
import { cancelBooking, getBookingByConfirmationCode } from "../utils/ApiFunctions"
import { BrowserRouter as Router, Route, Link } from 'react-router-dom';


const FindBooking = () => {
	const [confirmationCode, setConfirmationCode] = useState("")
	const [error, setError] = useState(null)
	const [successMessage, setSuccessMessage] = useState("")
	const [isLoading, setIsLoading] = useState(false)
	const [bookingInfo, setBookingInfo] = useState({
		id: "",
		bookingConfirmationCode: "",
		doctor: { id: "", specialite: ""},
		Date: "",
		date:"",
		heur: "",
		patientFullName: "",
		patientEmail: "",
		
	})

	const emptyBookingInfo = {
		id: "",
		bookingConfirmationCode: "",
		doctor: { id: "", specialite: "" },
		Date: "",
		date:"",
		heur: "",
		patientFullName: "",
		patientEmail: "",
		
	}
	const [isDeleted, setIsDeleted] = useState(false)

	const handleInputChange = (event) => {
		setConfirmationCode(event.target.value)
	}

	const handleFormSubmit = async (event) => {
		event.preventDefault()
		setIsLoading(true)

		try {
			const data = await getBookingByConfirmationCode(confirmationCode)
			setBookingInfo(data)
			setError(null)
		} catch (error) {
			setBookingInfo(emptyBookingInfo)
			if (error.response && error.response.status === 404) {
				setError(error.response.data.message)
			} else {
				setError(error.message)
			}
		}

		setTimeout(() => setIsLoading(false), 2000)
	}

	const handleBookingCancellation = async (bookingId) => {
		try {
			await cancelBooking(bookingInfo.id)
			setIsDeleted(true)
			setSuccessMessage("Booking has been cancelled successfully!")
			setBookingInfo(emptyBookingInfo)
			setConfirmationCode("")
			setError(null)
		} catch (error) {
			setError(error.message)
		}
		setTimeout(() => {
			setSuccessMessage("")
			setIsDeleted(false)
		}, 2000)
	}

	return (
		<>
			<div className="container mt-5 d-flex flex-column justify-content-center align-items-center">
				<h2 className="text-center mb-4">Find My Appointment</h2>
				<form onSubmit={handleFormSubmit} className="col-md-6">
					<div className="input-group mb-3">
						<input
							className="form-control"
							type="text"
							id="confirmationCode"
							name="confirmationCode"
							value={confirmationCode}
							onChange={handleInputChange}
							placeholder="Enter the booking confirmation code"
						/>

						<button type="submit" className="btn btn-hotel input-group-text">
							Find booking
						</button>
					</div>
				</form>

				{isLoading ? (
					<div>Finding your booking...</div>
				) : error ? (
					<div className="text-danger">Error: {error}</div>
				) : bookingInfo.bookingConfirmationCode ? (
					<div className="col-md-6 mt-4 mb-5">
						<h3>Appointment Information</h3>
						<p className="text-success">Confirmation Code: {bookingInfo.bookingConfirmationCode}</p>
                        <p>Your Name: {bookingInfo.patientFullName}</p>
						<p>Your Email : {bookingInfo.patientEmail}</p>
                        <p>Doctor Name : {bookingInfo.doctor.doctorName}</p>
						
						<p>
							 Appointment Date:{moment(bookingInfo.date).subtract(1, "month").format("MMM Do, YYYY")}

							{/*moment(bookingInfo.Date)/*.subtract(1, "month").format("MMM Do, YYYY")*/}
						</p>
						<p>
                            Hour:  {bookingInfo.heur}{/*moment(bookingInfo.Date).subtract(4,"hours").format("HH:mm")*/}
							

                        </p>
						

						{!isDeleted && (
							<button
								onClick={() => handleBookingCancellation(bookingInfo.id)}
								className="btn btn-danger">
								Cancel Appointment
							</button>
							
						)}
						<Link to={`/update-booking/${bookingInfo.id}`} className="btn btn-hotel btn-sm mx-2">Update</Link>

					</div>
				) : (
					<div>find Appointment...</div>
				)}

				{isDeleted && <div className="alert alert-success mt-3 fade show">{successMessage}</div>}
			</div>
		</>
	)
}

export default FindBooking