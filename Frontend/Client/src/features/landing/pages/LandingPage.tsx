import React from "react";
import FeatureSection from "../components/FeaturesSection";
import Testimonials from "../components/Testimonials";
import Footer from "../components/Footer";
import HeroSection from "../components/HeroSection";
import Navbar from "../components/Navbar";
import Workflow from "../components/Workflow";

const LandingPage: React.FC = () => {
  return (
    <div className="bg-white dark:bg-neutral-900 text-black dark:text-white">
      <Navbar />
      <div className="max-w-7xl mx-auto pt-20 px-6 space-y-20">
        <HeroSection />
        <FeatureSection />
        <Workflow />
        <Testimonials />
        <Footer />
      </div>
    </div>
  );
};

export default LandingPage;
