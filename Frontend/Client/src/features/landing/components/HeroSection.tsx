import React from "react";

const HeroSection: React.FC = () => {
  return (
    <div className="flex flex-col items-center mt-6 lg:mt-20 bg-white dark:bg-neutral-900 text-black dark:text-white">
      <h1 className="text-4xl sm:text-6xl lg:text-7xl text-center tracking-wide">
        One Stop Solution
        <span className="bg-gradient-to-r from-orange-500 to-red-800 text-transparent bg-clip-text">
          {" "}
          For School Management
        </span>
      </h1>
      <p className="mt-10 text-lg text-center text-neutral-500 dark:text-neutral-400 max-w-4xl">
        Experience seamless efficiency with our cutting-edge platform. From
        admissions to alumni relations, CtrlEdu optimizes every aspect of campus
        operations. Our innovative software simplifies complex administrative
        tasks, allowing you to focus on delivering exceptional education.
      </p>
      <div className="flex justify-center my-10">
        <a
          href="#"
          className="bg-gradient-to-r from-orange-500 to-orange-800 py-3 px-4 mx-3 rounded-md text-white"
        >
          Start for free
        </a>
        <a
          href="#"
          className="py-3 px-4 mx-3 rounded-md border border-neutral-500 dark:border-neutral-400 text-black dark:text-white"
        >
          Documentation
        </a>
      </div>
    </div>
  );
};

export default HeroSection;
