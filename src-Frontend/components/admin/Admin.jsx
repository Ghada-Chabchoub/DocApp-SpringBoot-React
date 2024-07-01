import React from "react"
import { Link } from "react-router-dom"

const Admin = () => {
	return (
		<section className="container mt-5">
			<h2>Welcome to Admin Panel</h2>
			<hr />
			<Link to={"/add-doctor"}>Manage doctors</Link> <br />
			<Link to={"/existing-bookings"}>Manage Bookings</Link> <br/>
			<Link to={"/existing-users"}>Manage users</Link> <br/>
            <Link to={"/register-doctor"}>Register new Doctor</Link>

		</section>
	)
}

export default Admin