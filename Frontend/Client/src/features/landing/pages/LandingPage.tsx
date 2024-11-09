// src/landing/LandingPage.tsx

import React from "react";
import Header from "../../common/components/header/Header";

const LandingPage: React.FC = () => {
  return (
    <div>
      <Header />
      {/* Add other sections for the landing page here */}
      <main>
        <h1>Welcome to CtrlEdu</h1>
        <p>Your all-in-one campus management system.</p>
        {/* Additional content can go here */}
      </main>
    </div>
  );
};

export default LandingPage;
