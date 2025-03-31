import React from 'react';
import './css/About.css';
import background from'../Resource/backgroundForAbout.jpg'
import TopNavigation from "../components/TopNavigation";

const About = () => {

    return (
        <div>
            <TopNavigation/>
            <div className="about-container" style={{backgroundImage:`url(${background})`}}>
                <div className="content">
                    <h1>About Us</h1>

                    <h2>Project Name: Traderbird</h2>
                    <h2>Team Name: Five for UBC</h2>

                    <h3>Team Members</h3>
                    <ul>
                        <li>Fengwei Huang</li>
                        <li>Yiran Wang</li>
                        <li>Guanxu Zhou</li>
                        <li>Yetao Li</li>
                        <li>Xiaohan Liu</li>
                    </ul>

                    <p>
                        Five men from the UBC ECE department dedicated to making life more convenient and learning easier for UBC students.
                    </p>

                    <h3>Team Mission</h3>
                    <p>Dedicated to making life more convenient and learning easier for UBC students!</p>

                    <p>
                        Our mission is to create a safer, more efficient, and campus-focused platform
                        that connects UBC students through second-hand trading.
                    </p>
                    <p>Contact Us: <a href={"mailto:huangfengwei56@gmail.com"}>huangfengwei56@gmail.com</a> </p>

                </div>
            </div>

        </div>
    );
};

export default About;