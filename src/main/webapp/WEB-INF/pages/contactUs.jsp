<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Task Track - Contact Us</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/contactUs.css" />
</head>
<body>
    <div class="banner">
    	<img src="${pageContext.request.contextPath}/resources/images/system/headBackground.jpg" alt="Desk Items" />
    </div>

    <div class="navigation">
    	<a href="home" class="nav-link">Home</a>
        <a href="userProfile" class="nav-link">Profile</a>
        <a href="viewTask" class="nav-link">Tasks</a>
        <a href="aboutUs" class="nav-link">About Us</a>
        <a href="contactUs" class="nav-link active">Contact Us</a>

    </div>

    <div class="content-container">
        <div class="left-panel">
            <h1>Contact Us</h1>
            <div class="logo-container">
                <img src="${pageContext.request.contextPath}/resources/images/system/logo.png" alt="Task Track Logo" class="logo">
            </div>
            <div class="contact-info-card">
                <h3>We'd Love to Hear From You</h3>
                <p>Have questions about our services? Want to learn more about how Task Track can help your team?</p>
                <p>Reach out to us using the contact form or directly through our support channels.</p>
                <div class="contact-quote">
                    <blockquote>"Effective communication is the cornerstone of successful project management."</blockquote>
                </div>
            </div>
        </div>

        <div class="right-panel">
            <h2>Get in Touch with Task Track</h2>
            <p class="tagline">We're here to help you achieve your project management goals</p>
            
            <div class="contact-info">
                <div class="contact-section">
                    <h3>Customer Support</h3>
                    <p><strong>Email:</strong> support@tasktrack.com</p>
                    <p><strong>Phone:</strong> (+977) 01-4588934</p>
                    <p><strong>Hours:</strong> Monday-Friday, 9AM-5PM</p>
                </div>
                
                <div class="contact-section">
                    <h3>Business Inquiries</h3>
                    <p><strong>Email:</strong> business@tasktrack.com</p>
                    <p><strong>Phone:</strong> (+977) 01-458012</p>
                </div>
            </div>
            
            <div class="contact-form">
                <h3>Send Us a Message</h3>
                <form>
                    <div class="form-group">
                        <label for="name">Name</label>
                        <input type="text" id="name" name="name" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="email">Email</label>
                        <input type="email" id="email" name="email" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="subject">Subject</label>
                        <input type="text" id="subject" name="subject" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="message">Message</label>
                        <textarea id="message" name="message" rows="5" required></textarea>
                    </div>
                    
                    <button type="submit" class="submit-btn">Send Message</button>
                </form>
            </div>
            
            <div class="office-locations">
                <h3>Our Offices</h3>
                <div class="locations-container">
                    <div class="location">
                        <h4>Headquarters</h4>
                        <p>Ramshah Path</p>
                        <p>Alfa Building</p>
                        <p>Putalisadak, KTM 94105</p>
                    </div>
                    
                    <div class="location">
                        <h4>West Region Office</h4>
                        <p>Street 04</p>
                        <p>Task Track Office</p>
                        <p>Pokhara, PKR 10022</p>
                    </div>
                    
                    <div class="location">
                        <h4>East Region Office</h4>
                        <p>RK Tower</p>
                        <p>Dharan, DH 19075</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <footer>
        <p>&copy; 2025 Task Track. All rights reserved.</p>
    </footer>
</body>
</html>