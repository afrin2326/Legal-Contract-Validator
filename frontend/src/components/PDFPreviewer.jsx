import React, { useEffect, useMemo } from 'react';
import { Worker, Viewer } from '@react-pdf-viewer/core';
import { defaultLayoutPlugin } from '@react-pdf-viewer/default-layout';
import { searchPlugin } from '@react-pdf-viewer/search';

import '@react-pdf-viewer/core/lib/styles/index.css';
import '@react-pdf-viewer/default-layout/lib/styles/index.css';
import '@react-pdf-viewer/search/lib/styles/index.css';

export default function PDFPreviewer({ fileUrl, highlightRange }) {
    const defaultLayoutPluginInstance = defaultLayoutPlugin();
    const searchPluginInstance = searchPlugin();
    const { highlight } = searchPluginInstance;

    useEffect(() => {
        const timer = setTimeout(() => {
            if (!highlightRange) return;

            const raw = highlightRange.text || highlightRange.description || '';
            const cleanSearchText = raw
                .replace(/\n/g, ' ')
                .replace(/\s+/g, ' ')
                .trim();

            if (cleanSearchText.length > 3) {
                highlight({
                    keyword: cleanSearchText,
                    matchCase: false,
                    wholeWords: false,
                });
                console.log('Highlighter triggered for:', cleanSearchText);
            }
        }, 800);

        return () => clearTimeout(timer);
    }, [highlightRange, highlight]);

    const workerUrl = useMemo(
        () => 'https://unpkg.com/pdfjs-dist@3.4.120/build/pdf.worker.min.js',
        []
    );

    return (
        <div className="h-full w-full relative">
            <Worker workerUrl={workerUrl}>
                <Viewer
                    fileUrl={fileUrl}
                    plugins={[defaultLayoutPluginInstance, searchPluginInstance]}
                />
            </Worker>
        </div>
    );
}