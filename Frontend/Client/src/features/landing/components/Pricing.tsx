import React from "react";
import { CheckCircle2 } from "lucide-react";
import { pricingOptions } from "../../../constants";

type PricingOption = {
  title: string;
  price: string;
  features: string[];
};

const Pricing: React.FC = () => {
  return (
    <div className="mt-20 bg-white dark:bg-neutral-900 text-black dark:text-white">
      <h2 className="text-3xl sm:text-5xl lg:text-6xl text-center my-8 tracking-wide">
        Pricing
      </h2>
      <div className="flex flex-wrap">
        {pricingOptions.map((option: PricingOption, index: number) => (
          <div key={index} className="w-full sm:w-1/2 lg:w-1/3 p-2">
            <div className="p-10 border border-neutral-700 dark:border-neutral-600 rounded-xl bg-neutral-100 dark:bg-neutral-800">
              <p className="text-4xl mb-8">
                {option.title}
                {option.title === "Pro" && (
                  <span className="bg-gradient-to-r from-orange-500 to-red-400 text-transparent bg-clip-text text-xl mb-4 ml-2">
                    (Most Popular)
                  </span>
                )}
              </p>
              <p className="mb-8">
                <span className="text-5xl mt-6 mr-2">{option.price}</span>
                <span className="text-neutral-500 dark:text-neutral-400 tracking-tight">
                  /Month
                </span>
              </p>
              <ul>
                {option.features.map((feature: string, index: number) => (
                  <li key={index} className="mt-8 flex items-center">
                    <CheckCircle2 className="text-orange-500 dark:text-orange-400" />
                    <span className="ml-2">{feature}</span>
                  </li>
                ))}
              </ul>
              <a
                href="#"
                className="inline-flex justify-center items-center text-center w-full h-12 p-5 mt-20 tracking-tight text-xl text-orange-900 dark:text-orange-300 hover:bg-orange-900 hover:text-white dark:hover:bg-orange-800 border border-orange-900 dark:border-orange-500 rounded-lg transition duration-200"
              >
                Subscribe
              </a>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Pricing;
