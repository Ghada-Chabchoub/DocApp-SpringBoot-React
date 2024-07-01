import React, { useEffect, useState } from "react"
import { deleteUser, getBookingsByUserEmail , getUser } from "../utils/ApiFunctions"
import { useNavigate } from "react-router-dom"
import moment from "moment"
import { BrowserRouter as Router, Route, Link } from 'react-router-dom';



const Profile = () => {
	
	const [user, setUser] = useState({
		id: "",
		email: "",
		name: "",
		username: "",
		roles: [{ id: "", name: "" }]
	})
	


	const [bookings, setBookings] = useState([
		{
			id: "",
			doctor: { email: "", specialite: "" },
			date: "",
			heur: "",
			bookingConfirmationCode: "",
			Date:""
		}
	])
	const [message, setMessage] = useState("")
	const [errorMessage, setErrorMessage] = useState("")
	const navigate = useNavigate()

	const userEmail = localStorage.getItem("userEmail")

	useEffect(() => {
		const fetchUser = async () => {
			try {
				const userData = await getUser(userEmail)
				setUser(userData)
			} catch (error) {
				console.error(error)
			}
		}

		fetchUser()
	}, [userEmail])//Si userEmail reste inchangé, 
	//l'effet ne se déclenchera pas à nouveau, 
	//ce qui évite les appels inutiles à fetchUser.

	useEffect(() => {
		const fetchBookings = async () => {
			try {
				const response = await getBookingsByUserEmail(userEmail)
				setBookings(response)
			} catch (error) {
				console.error("Error fetching bookings:", error.message)
				setErrorMessage(error.message)
			}
		}

		fetchBookings()
	}, [userEmail])

	const handleDeleteAccount = async () => {
		const confirmed = window.confirm(
			"Are you sure you want to delete your account? This action cannot be undone."
		)
		if (confirmed) {
			await deleteUser(userEmail)
				.then((response) => {
					setMessage(response.data)
					localStorage.removeItem("userEmail")
					localStorage.removeItem("userRole")
					navigate("/")
					window.location.reload()
				})
				.catch((error) => {
					setErrorMessage(error.data)
				})
		}
	}




	return (
		<div className="container">
			{errorMessage && <p className="text-danger">{errorMessage}</p>}
			{message && <p className="text-danger">{message}</p>}
			{user ? (
				<div className="card p-5 mt-5" style={{ backgroundColor: "whitesmoke" }}>
					<h4 className="card-title text-center">User Information</h4>
					<div className="card-body">
						<div className="col-md-10 mx-auto">
							<div className="card mb-3 shadow">
								<div className="row g-0">
									<div className="col-md-2">
										<div className="d-flex justify-content-center align-items-center mb-4">
											<img
												src="https://themindfulaimanifesto.org/wp-content/uploads/2020/09/male-placeholder-image.jpeg"
												alt="Profile"
												className="rounded-circle"
												style={{ width: "150px", height: "150px", objectFit: "cover" }}
											/>
										</div>
									</div>

									<div className="col-md-10">
										<div className="card-body">
											<div className="form-group row">
												<label className="col-md-2 col-form-label fw-bold">ID:</label>
												<div className="col-md-10">
													<p className="card-text">{user.id}</p>
												</div>
											</div>
											<hr />

											<div className="form-group row">
												<label className="col-md-2 col-form-label fw-bold">First Name:</label>
												<div className="col-md-10">
													<p className="card-text">{user.name}</p>
												</div>
											</div>
											<hr />

											<div className="form-group row">
												<label className="col-md-2 col-form-label fw-bold">Last Name:</label>
												<div className="col-md-10">
													<p className="card-text">{user.username}</p>
												</div>
											</div>
											<hr />

											<div className="form-group row">
												<label className="col-md-2 col-form-label fw-bold">Email:</label>
												<div className="col-md-10">
													<p className="card-text">{user.email}</p>
												</div>
											</div>
											<hr />

											<div className="form-group row">
												<label className="col-md-2 col-form-label fw-bold">Role:</label>
												<div className="col-md-10">
													<ul className="list-unstyled">
														{user.roles.map((role) => (
															<li key={role.id} className="card-text">
																{role.name}
															</li>
														))}
													</ul>
												</div>
											</div>
											<div className="d-flex justify-content-center mt-3">
                                              <Link to="/edit-profile" className="btn btn-hotel btn-sm mx-2">Edit</Link>
                                              <button className="btn btn-danger btn-sm mx-2" onClick={handleDeleteAccount}>Delete Account</button>
                                            </div>



										</div>
									</div>
								</div>
							</div>

							<h4 className="card-title text-center">Your Booking History</h4>

							{bookings.length > 0 ? (
								<table className="table table-bordered table-hover shadow">
									<thead>
										<tr>
											<th scope="col">Booking ID</th>
											<th scope="col">doctor ID</th>
											<th scope="col">Speciality</th>
											<th scope="col"> Date</th>
											<th scope="col">Hour</th>
											<th scope="col">Confirmation Code</th>
										</tr>
									</thead>
									<tbody>
										{bookings.map((booking, index) => (
											<tr key={index}>
												<td>{booking.id}</td>
												<td>{booking.doctor.id}</td>
												<td>{booking.doctor.specialite}</td>
												<td>
												{moment(booking.date).format("MMM Do, YYYY")}
												</td>
												<td>
												{/*moment(booking.Date).subtract(5,"hours").format("HH:mm")*/}

                                                  {booking.heur}

                                                 </td>
												<td>{booking.bookingConfirmationCode}</td>
											</tr>
										))}
									</tbody>
								</table>
							) : (
								<p>You have not made any bookings yet.</p>
							)}

							<div className="d-flex justify-content-center">
								
							</div>
						</div>
					</div>
				</div>
			) : (
				<p>Loading user data...</p>
			)}
		</div>
	)
}

export default Profile