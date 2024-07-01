import React, { useEffect, useState } from "react"
import { deleteUser, getBookingsByDoctorEmail , getUser } from "../utils/ApiFunctions"
import { useNavigate } from "react-router-dom"
import moment from "moment"
import { format } from 'date-fns';
import { BrowserRouter as Router, Route, Link } from 'react-router-dom';




const DoctorBookingList = () => {

	
	const [user, setUser] = useState({
		id: "",
		email: "",
		name: "",
		username: "",
		roles: [{ id: "", name: "" }]
		
	})
	const [isDeleted, setIsDeleted] = useState(false)

	


	const [bookings, setBookings] = useState([
		{
			id: "",
			date: "",
			heur: "",
            user: { name: "", email: "" },
			bookingConfirmationCode: ""
            
		}
	])


	const userEmail = localStorage.getItem("userEmail")
   // const id = localStorage.getItem("id")

	

	useEffect(() => {
		const fetchBookings = async () => {
			try {
				const response = await getBookingsByDoctorEmail(userEmail)
				setBookings(response)
			} catch (error) {
				console.error("Error fetching bookings:", error.message)
				setErrorMessage(error.message)
			}
		}

		fetchBookings()
	}, [userEmail])



	return (
		<div className="container">
			{user ? (
				<div>
					<h4 className="card-title text-center">Your Appointment </h4>

					{bookings.length > 0 ? (
						<table className="table table-bordered table-hover shadow">
							<thead>
								<tr>
									<th scope="col">Booking ID</th>
									
									<th scope="col">Patient Name</th>
									<th scope="col">patient email</th>
									<th scope="col"> Date</th>
									<th scope="col">Hour</th>
									<th scope="col">Action</th>

									
								</tr>
							</thead>
							<tbody>
								{bookings.map((booking, index) => (
									<tr key={index}>
										<td>{booking.id}</td>
										
										<td>{booking.user.name}</td>
										<td>{booking.user.email}</td>
										<td>
											{moment(booking.date).format("MMM Do, YYYY")}
										</td>
										
											
										<td>{booking.heur}</td>
										<td>
										{!isDeleted && (
							<button
								onClick={() => handleBookingCancellation(booking.id)}
								className="btn btn-danger">
								Cancel Appointment
							</button>
							
						)}
						<Link to={`/update-booking/${booking.id}`} className="btn btn-hotel btn-sm mx-2">Update</Link>

										
										
						</td></tr>
								))}
							</tbody>
						</table>
					) : (
						<p>You have not  any appointment yet.</p>
					)}

					<div className="d-flex justify-content-center">
						
					</div>
				</div>
			) : (
				<p>Loading user data...</p>
			)}
		</div>
	)
}

export default DoctorBookingList
