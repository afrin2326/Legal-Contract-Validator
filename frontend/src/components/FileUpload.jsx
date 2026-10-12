import React, { useCallback } from 'react';
import { useDropzone } from 'react-dropzone';
import { Upload, AlertCircle } from 'lucide-react';
import axios from 'axios';
import toast from 'react-hot-toast';

export default function FileUpload({ setResult, setLoading, setUploadedFile }) {

  const onDrop = useCallback(async (acceptedFiles) => {
    const file = acceptedFiles[0];
    if (!file) return;

    if (file.type !== 'application/pdf') {
      toast.error('Only PDF files are allowed!');
      return;
    }
    if (file.size > 10 * 1024 * 1024) {
      toast.error('File size must be less than 10MB!');
      return;
    }

    setUploadedFile(file);

    const formData = new FormData();
    formData.append('file', file);
    setLoading(true);

    try {
      // proxy use করছি — package.json এ "proxy" সেট করা আছে
      const response = await axios.post('/api/contracts/validate', formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
        timeout: 60000,
      });

      setResult(response.data);
      toast.success('Analysis Completed Successfully!');
    } catch (error) {
      console.error('Upload error:', error);
      toast.error(
        error.response?.status === 500
          ? 'Server error. Check backend logs.'
          : 'Analysis failed! Please try again.'
      );
    } finally {
      setLoading(false);
    }
  }, [setResult, setLoading, setUploadedFile]);

  const { getRootProps, getInputProps, isDragActive } = useDropzone({
    onDrop,
    accept: { 'application/pdf': ['.pdf'] },
    maxFiles: 1,
  });

  return (
    <div className="max-w-3xl mx-auto">
      <div
        {...getRootProps()}
        className={`
          border-4 border-dashed rounded-3xl p-12 text-center cursor-pointer transition-all duration-300
          ${isDragActive
            ? 'border-indigo-500 bg-indigo-50 scale-105 shadow-xl'
            : 'border-slate-300 hover:border-indigo-400 hover:bg-slate-50 shadow-sm'}
        `}
      >
        <input {...getInputProps()} />
        <div className={`w-24 h-24 mx-auto mb-6 rounded-full flex items-center justify-center transition-colors ${isDragActive ? 'bg-indigo-600 text-white' : 'bg-slate-100 text-slate-400'}`}>
          <Upload className="w-12 h-12" />
        </div>

        {isDragActive ? (
          <p className="text-2xl text-indigo-600 font-bold animate-bounce">Drop the file here!</p>
        ) : (
          <>
            <p className="text-3xl font-black text-slate-800 mb-3">Upload PDF Contract</p>
            <p className="text-slate-500 mb-6 font-medium">Drag &amp; Drop or click to browse files</p>
            <div className="flex justify-center space-x-6 text-xs font-bold text-slate-400 uppercase tracking-widest">
              <span>✓ PDF Only</span>
              <span>✓ Max 10MB</span>
              <span>✓ Instant Audit</span>
            </div>
          </>
        )}
      </div>

      <div className="mt-8 bg-slate-900 rounded-2xl p-5 flex items-start space-x-4 shadow-lg">
        <div className="bg-indigo-500/20 p-2 rounded-lg">
          <AlertCircle className="w-5 h-5 text-indigo-400" />
        </div>
        <div className="text-sm">
          <p className="font-bold text-white mb-1 uppercase tracking-tighter">System Status: Ready</p>
          <p className="text-slate-400 leading-relaxed">
            Your document will be processed through our
            <span className="text-indigo-400 font-mono"> Drools Rules Engine</span> for legal compliance check.
          </p>
        </div>
      </div>
    </div>
  );
}