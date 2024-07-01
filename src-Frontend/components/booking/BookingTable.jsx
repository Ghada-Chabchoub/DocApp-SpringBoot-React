import { parseISO } from "date-fns"
import React, { useState, useEffect } from "react"
import DateSlider from "../common/DateSlider"
import moment from "moment"


const BookingTable = ({ bookingInfo, handleBookingCancellation }) => {
	const [filteredBookings, setFilteredBookings] = useState(bookingInfo)

	const filterBooknigs = (startDate, endDate) => {
		let filtered = bookingInfo
		if (startDate && endDate) {
			filtered = bookingInfo.filter((booking) => {
				const bookingDate = parseISO(booking.date)
				
				return (
					bookingDate >= startDate && bookingDate <= endDate 
				)
			})
		}
		setFilteredBookings(filtered)
	}

	useEffect(() => {
		setFilteredBookings(bookingInfo)
	}, [bookingInfo])

	return (
		<section className="p-4">
			<DateSlider onDateChange={filterBooknigs} onFilterChange={filterBooknigs} />
			<table className="table table-bordered table-hover shadow">
				<thead>
					<tr>
						<th>S/N</th>
						<th>Booking ID</th>
						<th>Doctor Email</th>
                        <th>Doctor Name</th>
						<th>Specialite</th>
						<th> Date</th>
						<th>Hour</th>
						<th>Patient Name</th>
						<th>Patient Email</th>
						<th>Confirmation Code</th>
						<th colSpan={2}>Actions</th>
					</tr>
				</thead>
				<tbody className="text-center">
					{filteredBookings.map((booking, index) => (
						<tr key={booking.id}>
							<td>{index + 1}</td>
							<td>{booking.id}</td>
							<td>{booking.doctor.email}</td>
                            <td>{booking.doctor.doctorName}</td>
							<td>{booking.doctor.specialite}</td>
							{moment(booking.date).subtract(1, "month").format("MMM Do, YYYY")}
							<td>{booking.heur}</td>
							<td>{booking.patientFullName}</td>
							<td>{booking.patientEmail}</td>
							<td>{booking.bookingConfirmationCode}</td>
							<td>
								<button
									className="btn btn-danger btn-sm"
									onClick={() => handleBookingCancellation(booking.id)}>
									Cancel
								</button>
							</td>
						</tr>
					))}
				</tbody>
			</table>
			{filterBooknigs.length === 0 && <p> No booking found for the selected date</p>}
		</section>
	)
}

export default BookingTable