import React from "react";
import { motion, useAnimation } from "framer-motion";
import { useInView } from "react-intersection-observer";
import { features } from "../../../constants";

type Feature = {
  icon: React.ReactNode;
  text: string;
  description: string;
};

const FeatureSection: React.FC = () => {
  // Animation controls for each row
  const controlsRow1 = useAnimation();
  const controlsRow2 = useAnimation();

  // Intersection observers for each row
  const { ref: refRow1, inView: inViewRow1 } = useInView({
    triggerOnce: false,
    threshold: 0.1,
  });
  const { ref: refRow2, inView: inViewRow2 } = useInView({
    triggerOnce: false,
    threshold: 0.1,
  });

  // Trigger animations for each row based on visibility
  React.useEffect(() => {
    if (inViewRow1) {
      controlsRow1.start("visible");
    } else {
      controlsRow1.start("hidden");
    }
  }, [controlsRow1, inViewRow1]);

  React.useEffect(() => {
    if (inViewRow2) {
      controlsRow2.start("visible");
    } else {
      controlsRow2.start("hidden");
    }
  }, [controlsRow2, inViewRow2]);

  // Define animation variants
  const itemVariants = {
    hidden: { opacity: 0, y: 50 },
    visible: (index: number) => ({
      opacity: 1,
      y: 0,
      transition: { duration: 0.5, delay: index * 0.1 },
    }),
  };

  return (
    <div className="relative mt-20 border-b border-neutral-800 min-h-[800px]">
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

      {/* First row of features */}
      <div ref={refRow1} className="flex flex-wrap mt-10 lg:mt-16">
        {features.slice(0, 3).map((feature: Feature, index: number) => (
          <motion.div
            key={index}
            className="w-full sm:w-1/2 lg:w-1/3"
            initial="hidden"
            animate={controlsRow1}
            custom={index}
            variants={itemVariants}
          >
            <div className="flex mb-10">
              <div className="flex mx-6 h-10 w-10 p-2 bg-neutral-900 text-orange-700 justify-center items-center rounded-full">
                {feature.icon}
              </div>
              <div>
                <h5 className="mt-1 mb-6 text-xl">{feature.text}</h5>
                <p className="text-md p-2 mb-20 text-neutral-500">
                  {feature.description}
                </p>
              </div>
            </div>
          </motion.div>
        ))}
      </div>

      {/* Second row of features */}
      <div ref={refRow2} className="flex flex-wrap mt-4 lg:mt-8">
        {features.slice(3, 6).map((feature: Feature, index: number) => (
          <motion.div
            key={index}
            className="w-full sm:w-1/2 lg:w-1/3"
            initial="hidden"
            animate={controlsRow2}
            custom={index}
            variants={itemVariants}
          >
            <div className="flex mb-10">
              <div className="flex mx-6 h-10 w-10 p-2 bg-neutral-900 text-orange-700 justify-center items-center rounded-full">
                {feature.icon}
              </div>
              <div>
                <h5 className="mt-1 mb-6 text-xl">{feature.text}</h5>
                <p className="text-md p-2 mb-20 text-neutral-500">
                  {feature.description}
                </p>
              </div>
            </div>
          </motion.div>
        ))}
      </div>
    </div>
  );
};

export default FeatureSection;
