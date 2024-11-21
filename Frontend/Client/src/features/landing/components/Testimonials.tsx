import React from "react";
import { motion } from "framer-motion";
import { testimonials } from "../../../constants";

type Testimonial = {
  text: string;
  image: string;
  user: string;
  company: string;
};

const Testimonials: React.FC = () => {
  return (
    <div className="mt-20 tracking-wide bg-white dark:bg-neutral-900 text-black dark:text-white">
      <h2 className="text-3xl sm:text-5xl lg:text-6xl text-center my-10 lg:my-20">
        What People Are Saying
      </h2>
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6 max-w-7xl mx-auto px-4">
        {testimonials.map((testimonial: Testimonial, index: number) => (
          <motion.div
            key={index}
            className="bg-neutral-100 dark:bg-neutral-800 rounded-md p-6 text-md border border-neutral-300 dark:border-neutral-700 font-light flex flex-col justify-between"
            whileHover={{
              scale: 1.05, // Enlarge the card slightly
              boxShadow: "0px 8px 20px rgba(0, 0, 0, 0.2)", // Add shadow for pop-up effect
              transition: { duration: 0.3 }, // Smooth transition
            }}
          >
            <p className="text-neutral-700 dark:text-neutral-300 mb-4">
              {testimonial.text}
            </p>
            <div className="flex items-center mt-4">
              <img
                className="w-12 h-12 mr-4 rounded-full border border-neutral-300 dark:border-neutral-500"
                src={testimonial.image}
                alt={`${testimonial.user}'s profile`}
              />
              <div>
                <h6 className="font-semibold text-neutral-800 dark:text-neutral-200">
                  {testimonial.user}
                </h6>
                <span className="text-sm font-normal italic text-neutral-500 dark:text-neutral-400">
                  {testimonial.company}
                </span>
              </div>
            </div>
          </motion.div>
        ))}
      </div>
    </div>
  );
};

export default Testimonials;
