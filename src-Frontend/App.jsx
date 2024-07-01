import React from "react"
import "../node_modules/bootstrap/dist/css/bootstrap.min.css"
import "/node_modules/bootstrap/dist/js/bootstrap.min.js"
import AddDoctor from "./components/doctor/AddDoctor"
import ExistingDoctors from "./components/doctor/ExistingDoctors"
import { BrowserRouter as Router, Routes, Route } from "react-router-dom"
import Home from "./components/home/Home"
import EditDoctor from "./components/doctor/EditDoctor"
import NavBar from "./components/layout/NavBar"
import Footer from "./components/layout/Footer"
import DoctorListing from "./components/doctor/DoctorListing"
import Admin from "./components/admin/Admin"
import BookingSuccess from "./components/booking/BookingSuccess"
import BookingForm from "./components/booking/BookingForm"
import AllBookings from "./components/booking/AllBookings"
import FindBooking from "./components/booking/FindBooking"
import Login from "./components/auth/Login"
import Registration from "./components/auth/Registration"
import Profile from "./components/auth/Profile"
import { AuthProvider } from "./components/auth/AuthProvider"
import RequireAuth from "./components/auth/RequireAuth"
import RegisterDoctor from "./components/auth/RegisterDoctor"
import AllUsers from "./components/users/AllUsers"
import EditProfil from "./components/users/EditProfil"
import DoctorBookingList from "./components/booking/DoctorBookingList"
import EditBooking from "./components/booking/EditBooking"



function App() {
	return(
		<AuthProvider>
	
	<main>
		<Router>
			<NavBar/>
			<Routes>
				<Route Route path="/" element={<Home />} />
				<Route path="/edit-doctor/:doctorId" element={<EditDoctor />} />
				<Route path="/existing-doctors" element={<ExistingDoctors />} />
				<Route path="/add-doctor" element={<AddDoctor />} />
				<Route path="/browse-all-doctors" element={<DoctorListing />} />
				<Route path="/admin" element={<Admin />} />
				<Route path="/book-app/:doctorId" element=
				{<RequireAuth>
					<BookingForm />
				</RequireAuth>} />
                <Route path="/booking-success" element={<BookingSuccess />} />
				<Route path="/find-appointment" element={<FindBooking />} />
                <Route path="/login" element={<Login />} />
				<Route path="/register" element={<Registration />} />
				<Route path="/register-doctor" element={<RegisterDoctor />} />
				<Route path="/existing-bookings" element={<AllBookings />} />
				<Route path="/existing-users" element={<AllUsers />} />
                <Route path="/profile" element={<Profile />} />
			    <Route path="/logout" element={<FindBooking />} />
                <Route path="/edit-profile" element={<EditProfil />} />
				<Route path="/doctor/booking-list" element={<DoctorBookingList />} />
				<Route path="/update-booking/:bookingId" element={<EditBooking />} />


					
         </Routes>
         </Router>
		 <Footer />
    </main>
 </AuthProvider>
  
	)
}

export default App