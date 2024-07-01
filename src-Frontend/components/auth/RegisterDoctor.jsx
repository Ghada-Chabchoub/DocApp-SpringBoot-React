import React, { useState } from "react"
import { registerDoctor } from "../utils/ApiFunctions"
import { Link } from "react-router-dom"

const Registration = () => {
	const [registration, setRegistration] = useState({
		name: "",
		username: "",
		email: "",
		password: ""
	})

	const [errorMessage, setErrorMessage] = useState("")
	const [successMessage, setSuccessMessage] = useState("")

	const handleInputChange = (e) => {
		setRegistration({ ...registration, [e.target.name]: e.target.value })
	}

	const handleRegistration = async (e) => {
		e.preventDefault()
		try {
			const result = await registerDoctor(registration)
			setSuccessMessage(result)
			setErrorMessage("")
			setRegistration({ name: "", username: "", email: "", password: "" })
		} catch (error) {
			setSuccessMessage("")
			setErrorMessage(`Registration error : ${error.message}`)
		}
		setTimeout(() => {
			setErrorMessage("")
			setSuccessMessage("")
		}, 5000)
	}

	return (
		<section className="container col-6 mt-5 mb-5">
			{errorMessage && <p className="alert alert-danger">{errorMessage}</p>}
			{successMessage && <p className="alert alert-success">{successMessage}</p>}

			<h2>Register a new Doctor</h2>
			<form onSubmit={handleRegistration}>
				<div className="mb-3 row">
					<label htmlFor="name" className="col-sm-2 col-form-label">
						Doctor First Name
					</label>
					<div className="col-sm-10">
						<input
							id="name"
							name="name"
							type="text"
							className="form-control"
							value={registration.name}
							onChange={handleInputChange}
						/>
					</div>
				</div>

				<div className="mb-3 row">
					<label htmlFor="username" className="col-sm-2 col-form-label">
						Doctor Last Name
					</label>
					<div className="col-sm-10">
						<input
							id="username"
							name="username"
							type="text"
							className="form-control"
							value={registration.username}
							onChange={handleInputChange}
						/>
					</div>
				</div>

				<div className="mb-3 row">
					<label htmlFor="email" className="col-sm-2 col-form-label">
						Doctor Email
					</label>
					<div className="col-sm-10">
						<input
							id="email"
							name="email"
							type="email"
							className="form-control"
							value={registration.email}
							onChange={handleInputChange}
						/>
					</div>
				</div>

				<div className="mb-3 row">
					<label htmlFor="password" className="col-sm-2 col-form-label">
						Doctor Password
					</label>
					<div className="col-sm-10">
						<input
							type="password"
							className="form-control"
							id="password"
							name="password"
							value={registration.password}
							onChange={handleInputChange}
						/>
					</div>
				</div>
				<div className="mb-3">
					<button type="submit" className="btn btn-hotel" style={{ marginRight: "10px" }}>
						Register
					</button>
					
				</div>
			</form>
		</section>
	)
}

export default Registration