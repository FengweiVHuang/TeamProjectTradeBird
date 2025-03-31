// `frontend/src/pages/Login.js`
import React from 'react';
import AuthForm from '../components/AuthForm';
import {useNavigate} from 'react-router-dom';
import axiosApi from "../util/AxiosRequests";
import './css/Login.css';
import TopNavigation from "../components/TopNavigation"; // 引入 Logo

function Login() {
    const navigate = useNavigate();

    const handleLogin = async (user) => {
        axiosApi.post('/users/login', {
                username: user.username,
                password: user.password
            }
        ).then(response => {
            alert('Login successful');
            if(getURL())
            {
                window.location.assign(getURL());
            }
            else
                navigate('/v2/detail');
        }).catch(error => {
            alert('Login failed');
            console.error(error);
        });
    };

    return(
        <div>
            <TopNavigation/>
            <div className="login-container">

                <AuthForm onSubmit={handleLogin} buttonText="Login"/>
            </div>

        </div>
    );
}

function getURL() {
    return new URLSearchParams(window.location.search).get("targetURL");
}
export default Login;