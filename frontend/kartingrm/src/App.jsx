import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import "./App.css";
import Home from "./components/Home";
import ClientRegister from "./components/ClientRegister";
import ClientLogin from "./components/ClientLogin.jsx";
import ClientMain from "./components/ClientMain";
import StaffRegister from "./components/StaffRegister.jsx";
import StaffLogin from "./components/StaffLogin.jsx";
import StaffMain from "./components/StaffMain";
import MakeBooking from "./components/MakeBooking.jsx";
import MyBookings from "./components/MyBookings.jsx";
import ListAllBookings from "./components/ListAllBookings.jsx";
import ViewBookings from "./components/ViewBookings.jsx";
import Rack from "./components/Rack";
import SeeDetails from "./components/SeePaymentDetails.jsx";
import ReportByType from "./components/ReportByType.jsx";
import ReportByQuantity from "./components/ReportByQuantity.jsx";

function App() {
    return (
        <Router>
            <div className="container">
                <Routes>
                    <Route path="/home" element={<Home />} />
                    <Route path="/" element={<Home />} />
                    <Route path="/client-register" element={<ClientRegister />} />
                    <Route path="/client-login" element={<ClientLogin />} />
                    <Route path="/client-main" element={<ClientMain />} />
                    <Route path="/staff-register" element={<StaffRegister />} />
                    <Route path="/staff-login" element={<StaffLogin />} />
                    <Route path="/staff-main" element={<StaffMain />} />
                    <Route path="/make-booking" element={<MakeBooking />} />
                    <Route path="/my-bookings" element={<MyBookings />} />
                    <Route path="/list-all-bookings" element={<ListAllBookings />} />
                    <Route path="/view-bookings" element={<ViewBookings />} />
                    <Route path="/view-bookings/:id" element={<ViewBookings />} />
                    <Route path="/rack" element={<Rack />} />
                    <Route path="/see-payment/:id" element={<SeeDetails />} />
                    <Route path="/report-by-type" element={<ReportByType />} />
                    <Route path="/report-by-quantity" element={<ReportByQuantity />} />
                </Routes>
            </div>
        </Router>
    );
}

export default App;

