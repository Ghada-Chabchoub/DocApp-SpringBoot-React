import React, { useState, useEffect } from "react";
import moment from "moment";
import Button from "react-bootstrap/Button";
import { useNavigate } from "react-router-dom";

const BookingSummary = ({ booking, isFormValid, onConfirm }) => {

  const [isBookingConfirmed, setIsBookingConfirmed] = useState(false);
  const navigate = useNavigate();

  const handleConfirmBooking = () => {
    if (!isFormValid) return; // Prevent confirmation if form is invalid
  
    setIsBookingConfirmed(true);
    onConfirm();
  };

  useEffect(() => {
    if (isBookingConfirmed) {
		console.log("isBookingConfirmed is true. Navigating...");
     navigate("/booking-success");
    }
  }, [isBookingConfirmed, navigate]);


  return (
    <div className="row">
      <div className="col-md-6"></div>
      <div className="card card-body mt-5">
        <h4 className="card-title hotel-color">Appointment Summary</h4>
        <p>
          Name: <strong>{booking.patientFullName}</strong>
        </p>
        <p>
          Email: <strong>{booking.patientEmail}</strong>
        </p>
        <p>
          Day: <strong>{moment(booking.date).format("MMM Do YYYY")}</strong>
        </p>
        <p>
          Hour: <strong>{booking.heur}</strong>        
		  </p>
      
     

        
<>

{!isBookingConfirmed ? (
    <Button variant="success" onClick={handleConfirmBooking}>
      Confirm
    </Button>
  ) : (
    <Button variant="success" disabled>
      <span className="spinner-border spinner-border-sm mr-2" role="status" aria-hidden="true"></span>
      Confirm Booking & get your confirmation code
    </Button>
  )}
</>
     
          </div>
    </div>
  );
};

export default BookingSummary;
