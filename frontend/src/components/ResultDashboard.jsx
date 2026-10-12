import React, { useState, useMemo } from 'react';
import RiskMeter from './RiskMeter';
import PDFPreviewer from './PDFPreviewer';
import { Download, ShieldCheck, Eye, ChevronRight, Lightbulb, CheckCircle } from 'lucide-react';
import jsPDF from 'jspdf';
import 'jspdf-autotable';

export default function ResultDashboard({ result, uploadedFile }) {
  const [activeHighlight, setActiveHighlight] = useState(null);
  const issues = result.allIssues || [];

  const fileUrl = useMemo(
    () => (uploadedFile ? URL.createObjectURL(uploadedFile) : null),
    [uploadedFile]
  );

  const cleanText = (text) => {
    if (!text) return 'N/A';
    return text.replace(/[^\x00-\x7F]/g, '');
  };

  const downloadPDF = () => {
    try {
      const doc = new jsPDF();
      doc.setFontSize(20);
      doc.setTextColor(67, 56, 202);
      doc.text('Contract Audit Report', 14, 22);

      const tableRows = issues.map((issue) => [
        cleanText(issue.severity),
        cleanText(issue.type),
        cleanText(issue.description),
        cleanText(issue.suggestedFix),
      ]);

      doc.autoTable({
        startY: 50,
        head: [['Severity', 'Type', 'Observation', 'Fix']],
        body: tableRows,
        theme: 'grid',
        headStyles: { fillColor: [67, 56, 202] },
      });

      doc.save(`Audit_Report_${result.fileName || 'contract'}.pdf`);
    } catch (e) {
      console.error('PDF export failed:', e);
    }
  };

  const getSeverityBadge = (severity) => {
    const badges = {
      CRITICAL: 'bg-red-100 text-red-700 border-red-200',
      HIGH: 'bg-orange-100 text-orange-700 border-orange-200',
      MEDIUM: 'bg-amber-100 text-amber-700 border-amber-200',
      LOW: 'bg-emerald-100 text-emerald-700 border-emerald-200',
    };
    return badges[severity] || 'bg-slate-100 text-slate-700 border-slate-200';
  };

  const formatSummary = (text) => {
    if (!text) return ['No summary available.'];
    return text
      .replace(/\*\*/g, '')
      .replace(/[✅🚩🚀🤖📊💡🔍⚠️]/g, '')
      .split('\n')
      .filter((line) => line.trim() !== '');
  };

  return (
    <div className="max-w-[1600px] mx-auto mt-6 px-4 pb-20 font-sans">
      <div className="flex flex-col md:flex-row justify-between items-center bg-white p-6 rounded-3xl shadow-sm border border-slate-200 mb-8">
        <div className="flex items-center space-x-4">
          <div className="bg-indigo-600 p-3 rounded-2xl shadow-lg">
            <ShieldCheck className="w-7 h-7 text-white" />
          </div>
          <div>
            <h2 className="text-2xl font-black text-slate-800 tracking-tight">Legal Audit Intelligence</h2>
            <p className="text-slate-400 text-[10px] font-bold uppercase tracking-widest">Drools Engine Active</p>
          </div>
        </div>
        <button
          onClick={downloadPDF}
          className="bg-slate-900 hover:bg-black text-white px-8 py-3 rounded-2xl flex items-center space-x-2 text-sm font-bold shadow-xl transition-all"
        >
          <Download className="w-4 h-4" />
          <span>Export Report</span>
        </button>
      </div>

      <div className="flex flex-col lg:flex-row gap-8">
        <div className="lg:w-1/2">
          <div className="sticky top-6">
            <div className="bg-slate-900 px-6 py-4 rounded-t-3xl flex items-center justify-between border-b border-slate-800">
              <span className="text-white text-xs font-black uppercase tracking-widest flex items-center gap-2">
                <Eye className="w-4 h-4 text-indigo-400" /> Document Insight
              </span>
              <span className="text-slate-500 text-[10px] font-mono">{result.fileName}</span>
            </div>
            <div className="h-[800px] border-2 border-slate-200 border-t-0 rounded-b-3xl overflow-hidden bg-white shadow-2xl">
              {fileUrl ? (
                <PDFPreviewer fileUrl={fileUrl} highlightRange={activeHighlight} />
              ) : (
                <div className="h-full flex items-center justify-center text-slate-400">
                  Loading Document...
                </div>
              )}
            </div>
          </div>
        </div>

        <div className="lg:w-1/2 space-y-6">
          <RiskMeter score={result.riskScore} />

          <div className="bg-white rounded-3xl shadow-sm border border-slate-200 overflow-hidden">
            <div className="bg-slate-50 px-8 py-5 border-b border-slate-100 flex items-center gap-3">
              <Lightbulb className="w-5 h-5 text-indigo-600" />
              <h3 className="text-sm font-black text-slate-700 uppercase tracking-widest">Executive Summary</h3>
            </div>
            <div className="p-8 bg-gradient-to-b from-white to-slate-50/30">
              <div className="space-y-4">
                {formatSummary(result.summary).map((line, i) => (
                  <div key={i} className="flex items-start gap-3 group">
                    <div className="mt-1.5 h-1.5 w-1.5 rounded-full bg-indigo-400 shrink-0"></div>
                    <p className="text-slate-600 text-sm leading-relaxed font-medium">{line}</p>
                  </div>
                ))}
              </div>
            </div>
          </div>

          <div className="bg-white rounded-3xl shadow-sm border border-slate-200 overflow-hidden">
            <div className="bg-slate-900 px-8 py-5">
              <h3 className="text-white font-black text-xs uppercase tracking-widest">Detailed Findings</h3>
            </div>
            <div className="divide-y divide-slate-100 max-h-[600px] overflow-y-auto custom-scrollbar">
              {issues.length === 0 && (
                <div className="p-8 text-slate-400 text-center">No issues found.</div>
              )}
              {issues.map((issue, idx) => (
                <div
                  key={idx}
                  onClick={() => {
                    const targetText = issue.text || issue.snippet || issue.description;
                    setActiveHighlight({
                      startIndex: issue.startIndex,
                      endIndex: issue.endIndex,
                      description: issue.description,
                      text: targetText,
                    });
                  }}
                  className={`p-8 cursor-pointer transition-all border-l-8 ${
                    activeHighlight?.startIndex === issue.startIndex
                      ? 'border-indigo-600 bg-indigo-50/50'
                      : 'border-transparent hover:bg-slate-50'
                  }`}
                >
                  <div className="flex items-center justify-between mb-4">
                    <div className="flex items-center gap-3">
                      <span className={`px-3 py-1 rounded-lg text-[10px] font-black border uppercase ${getSeverityBadge(issue.severity)}`}>
                        {issue.severity}
                      </span>
                      <span className="text-[10px] text-slate-400 font-bold uppercase tracking-widest">{issue.type}</span>
                    </div>
                    <ChevronRight className={`w-4 h-4 transition-transform ${activeHighlight?.startIndex === issue.startIndex ? 'rotate-90 text-indigo-600' : 'text-slate-300'}`} />
                  </div>

                  <h4 className="text-md font-bold text-slate-800 mb-4 leading-snug">{issue.description}</h4>

                  {issue.suggestedFix && (
                    <div className="bg-white rounded-2xl p-5 border border-slate-100 shadow-sm flex items-start gap-4">
                      <CheckCircle className="w-4 h-4 text-emerald-600 shrink-0" />
                      <div>
                        <p className="text-[10px] font-black text-slate-400 uppercase tracking-widest mb-1">Recommended Fix</p>
                        <p className="text-sm text-slate-600 leading-relaxed font-medium">{issue.suggestedFix}</p>
                      </div>
                    </div>
                  )}
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}