// `frontend/src/pages/Register.js`
import React, {useState} from 'react';
import StringInputText from "../components/StringInputText";
import {useNavigate} from "react-router-dom";
import axiosApi from "../util/AxiosRequests";
import './css/Register.css';
import logo from '../Resource/logo.png';
import TopNavigation from "../components/TopNavigation"; // 引入 Logo

function ResetPassword() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [reEnterPassword, setReEnterPassword] = useState('');
    const [verificationCode, setVerificationCode] = useState('');
    const navigate = useNavigate();

    const handleRegisterStage1 = async (uname) => {
        try {
            const response = await axiosApi.get(`/users/reset-password-stage-1/username=${uname}`)
            console.log(response);
            alert('Verification code sent');
        } catch (error) {
            alert('User not found');
            console.error(error);
        }
    }

    const handleRegisterStage2 = async (uname, vcode, pwd,rpwd) => {
        //console.log(uname, vcode, pwd,rpwd);
        if(rpwd!==pwd)
        {
            alert('Two passwords should be same');
        }
        try {
            const response = await axiosApi.post('/users/reset-password-stage-2', {
                username: uname,
                verificationCode: vcode,
                password: pwd
            });
            console.log(response);
            navigate('/v2/login');
            alert('Password reset successful');
        } catch (error) {
            console.error(error);
            alert('Password reset failed');
        }
    }

    return (
        <div>
            <TopNavigation/>
            <div className="register-container">
                <div className={"auth-form-register"}>
                    <div className="logo-container">
                        <img src={logo} alt="Logo"/>
                    </div>
                    <h1 style={{textAlign:"center"}}>Reset Password</h1>

                    <StringInputText label={"Username: "} onChange={(e) => setUsername(e.target.value)}/>
                    <button onClick={() => handleRegisterStage1(username)}>Get Verification Code</button>


                    <StringInputText label={"Verification Code :"}
                                     onChange={(e) => setVerificationCode(e.target.value)}/>
                    <StringInputText isPassword={true} label={"New Password :"} onChange={(e) => setPassword(e.target.value)}/>
                    <StringInputText isPassword={true} label={"Re-Enter New Password :"}
                                     onChange={(e) => setReEnterPassword(e.target.value)}/>

                    <a href="/v2/register" style={{width: "100%", textAlign: "right", marginBottom: "10px"}}>Does not have an account? Register</a>
                    <button
                        onClick={() => handleRegisterStage2(username, verificationCode, password, reEnterPassword)}>Reset Password
                    </button>
                </div>

            </div>
        </div>);
}

export default ResetPassword;