import React from 'react';
import {BrowserRouter as Router, Navigate, Route, Routes} from 'react-router-dom';
import Root from './pages/Root';
import Register from './pages/Register';
import Login from './pages/Login';
import 'bootstrap/dist/css/bootstrap.min.css';
import DetailPage from "./pages/Detail";
import About from "./pages/About";
import ResetPassword from "./pages/ResetPassword";

const App = () => {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Navigate to="/v2" />} />
                <Route path="/v2" element={<Root/>}/>
                <Route path="/v2/register" element={<Register/>}/>
                <Route path="/v2/login" element={<Login/>}/>
                <Route path="/v2/detail" element={<DetailPage/>}/>
                <Route path={"/v2/about"} element={<About/>}/>

                <Route path={"/v2/resetpassword"} element={<ResetPassword/>}/>

            </Routes>
        </Router>
    );
};

//<Route path="/v2/dictionary" element={<Dictionary/>}/>
//<Route path="/v2/home" element={<Home/>}/>
export default App;