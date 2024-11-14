import React from "react";
import { features } from "../../../constants";

type Feature = {
  icon: React.ReactNode;
  text: string;
  description: string;
};

const FeatureSection: React.FC = () => {
  return (
    <div className="relative mt-20 border-b border-neutral-800 min-h-[800px] bg-white dark:bg-neutral-900 text-black dark:text-white">
      <div className="text-center">
        <span className=" text-orange-500 rounded-full h-6 text-sm font-medium px-2 py-1 uppercase">
          CtrlEdu isn't limited to just schools—it's crafted for all types of
          educational institutions. Whether you manage a school, college, or
          university, our platform adapts to meet your unique needs, ensuring a
          smooth experience for administrators, educators, and students.
        </span>
        <h2 className="text-3xl sm:text-5xl lg:text-6xl mt-10 lg:mt-20 tracking-wide">
          Discover Our{" "}
          <span className="bg-gradient-to-r from-orange-500 to-orange-800 text-transparent bg-clip-text">
            Key Features
          </span>
        </h2>
      </div>
      <div className="flex flex-wrap mt-10 lg:mt-20">
        {features.map((feature: Feature, index: number) => (
          <div key={index} className="w-full sm:w-1/2 lg:w-1/3">
            <div className="flex">
              <div className="flex mx-6 h-10 w-10 p-2 bg-neutral-200 dark:bg-neutral-800 text-orange-700 justify-center items-center rounded-full">
                {feature.icon}
              </div>
              <div>
                <h5 className="mt-1 mb-6 text-xl">{feature.text}</h5>
                <p className="text-md p-2 mb-20 text-neutral-500 dark:text-neutral-400">
                  {feature.description}
                </p>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default FeatureSection;
