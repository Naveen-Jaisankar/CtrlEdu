import { CheckCircle2 } from "lucide-react";
import CtrlSuite from "../../../assets/workflow.png";
import { checklistItems } from "../../../constants";

const Workflow = () => {
  return (
    <div className="mt-20 bg-white dark:bg-neutral-900 text-black dark:text-white">
      <h2 className="text-3xl sm:text-5xl lg:text-6xl text-center mt-6 tracking-wide">
        Why Choose{" "}
        <span className="bg-gradient-to-r from-orange-500 to-orange-800 text-transparent bg-clip-text">
          CtrlEdu?
        </span>
      </h2>
      <div className="flex flex-wrap justify-center mt-10">
        <div className="p-4 w-full lg:w-2/3 flex justify-center">
          <img
            src={CtrlSuite}
            alt="CtrlSuite workflow illustration"
            className="rounded-lg shadow-lg dark:shadow-neutral-800"
          />
        </div>
        <div className="pt-10 w-full lg:w-1/3">
          {checklistItems.map((item, index) => (
            <div key={index} className="flex mb-12">
              <div className="text-green-400 mx-6 bg-neutral-200 dark:bg-neutral-800 h-10 w-10 p-2 flex justify-center items-center rounded-full">
                <CheckCircle2 className="text-green-500 dark:text-green-400" />
              </div>
              <div>
                <h5 className="mt-1 mb-2 text-xl text-neutral-800 dark:text-neutral-200">
                  {item.title}
                </h5>
                {/* <p className="text-md text-neutral-500 dark:text-neutral-400">{item.description}</p> */}
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default Workflow;
