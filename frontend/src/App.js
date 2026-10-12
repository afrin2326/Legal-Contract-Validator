import React, { useState } from 'react';
import FileUpload from './components/FileUpload';
import ResultDashboard from './components/ResultDashboard';
import Navbar from './components/Navbar';
import { Toaster } from 'react-hot-toast';

function App() {
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [uploadedFile, setUploadedFile] = useState(null); // নতুন স্টেট: অরিজিনাল ফাইল সেভ করার জন্য

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-purple-50">
      <Navbar />
      
      <main className="container mx-auto px-4 py-8">
        <div className="text-center mb-10">
          <h1 className="text-5xl font-bold bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent mb-4">
            Smart Contract Validator
          </h1>
          <p className="text-gray-600 text-lg max-w-2xl mx-auto">
            AI-Powered Legal Document Analysis • Instant Risk Assessment • Smart Recommendations
          </p>
          <p className="text-sm text-purple-600 mt-2 font-medium">
            🤖 Mock AI Mode (No API Key Required)
          </p>
        </div>
        
        {/* FileUpload-এ setUploadedFile পাস করা হয়েছে */}
        <FileUpload 
          setResult={setResult} 
          setLoading={setLoading} 
          setUploadedFile={setUploadedFile} 
        />
        
        {loading && (
          <div className="flex flex-col items-center justify-center my-12 space-y-4">
            <div className="animate-spin rounded-full h-16 w-16 border-t-4 border-b-4 border-purple-600"></div>
            <p className="text-purple-600 font-bold animate-pulse">Analyzing Document...</p>
          </div>
        )}
        
        {/* ResultDashboard-এ এখন uploadedFile প্রপস হিসেবে পাঠানো হচ্ছে */}
        {result && !loading && (
          <ResultDashboard result={result} uploadedFile={uploadedFile} />
        )}
      </main>
      
      <Toaster position="top-right" />
    </div>
  );
}

export default App;