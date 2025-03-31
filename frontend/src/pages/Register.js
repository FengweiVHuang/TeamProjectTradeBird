// `frontend/src/pages/Register.js`
import React, {useState} from 'react';
import StringInputText from "../components/StringInputText";
import {useNavigate} from "react-router-dom";
import axiosApi from "../util/AxiosRequests";
import './css/Register.css';
import logo from '../Resource/logo.png';
import TopNavigation from "../components/TopNavigation"; // 引入 Logo

function Register() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [reEnterPassword, setReEnterPassword] = useState('');
    const [email, setEmail] = useState('');
    const [verificationCode, setVerificationCode] = useState('');
    const navigate = useNavigate();

    const handleRegisterStage1 = async (uname, em) => {

        //console.log(uname, em);
        var re = /\S+@\S+\.ubc\.ca/;
        if(re.test(em) === false)
        {
            alert('Please use UBC email');
            return;
        }
        try {
            const response = await axiosApi.post('/users/register-stage-1',
                {
                    username: uname,
                    email: em
                })
            console.log(response);
            alert('Verification code sent');
        } catch (error) {
            console.error(error);
            alert(error.body);
        }
    }

    const handleRegisterStage2 = async (uname, vcode, pwd,rpwd) => {
        //console.log(uname, vcode, pwd,rpwd);
        if(rpwd!==pwd)
        {
            alert('Two passwords should be same');
        }
        try {
            const response = await axiosApi.post('/users/register-stage-2', {
                username: uname,
                verificationCode: vcode,
                password: pwd
            });
            console.log(response);
            navigate('/v2/login');
            alert('Registration successful');
        } catch (error) {
            console.error(error);
            alert('Registration failed');
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
                    <h1 style={{textAlign: "center"}}>Register</h1>

                    <StringInputText label={"Username: "} onChange={(e) => setUsername(e.target.value)}/>
                    <StringInputText label={"Email: "} onChange={(e) => setEmail(e.target.value)}/>
                    <button onClick={() => handleRegisterStage1(username, email)}>Get Verification Code</button>


                    <StringInputText label={"Verification Code :"}
                                     onChange={(e) => setVerificationCode(e.target.value)}/>
                    <StringInputText isPassword={true} label={"Password :"}
                                     onChange={(e) => setPassword(e.target.value)}/>
                    <StringInputText isPassword={true} label={"Re-Enter Password :"}
                                     onChange={(e) => setReEnterPassword(e.target.value)}/>

                    <a href="/v2/login" style={{width: "100%", textAlign: "right", marginBottom: "10px"}}>Already have
                        an account? Login</a>
                    <button
                        onClick={() => handleRegisterStage2(username, verificationCode, password, reEnterPassword)}>Register
                    </button>
                </div>

            </div>
        </div>);
}

export default Register;