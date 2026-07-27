import { Component } from 'react';
import { AlertTriangle } from 'lucide-react';

export default class ErrorBoundary extends Component {
  constructor(props) {
    super(props);
    this.state = { error: null, info: null };
  }

  static getDerivedStateFromError(error) {
    return { error };
  }

  componentDidCatch(error, info) {
    this.setState({ info });
    // eslint-disable-next-line no-console
    console.error('ErrorBoundary caught:', error, info);
  }

  render() {
    if (this.state.error) {
      return (
        <div className="max-w-2xl mx-auto px-4 py-16">
          <div className="border border-wine rounded-sm p-6 bg-wine/5">
            <div className="flex items-center gap-2 text-wine mb-3">
              <AlertTriangle size={20} />
              <h1 className="font-display text-xl">Something broke</h1>
            </div>
            <p className="font-mono text-sm text-ink mb-4 break-words">
              {this.state.error.name}: {this.state.error.message}
            </p>
            {this.state.info?.componentStack && (
              <pre className="text-xs text-ink-faint bg-paper-deep p-3 rounded-sm overflow-auto max-h-64 whitespace-pre-wrap">
                {this.state.info.componentStack}
              </pre>
            )}
            <button
              onClick={() => window.location.reload()}
              className="mt-4 bg-ink text-paper text-sm font-semibold px-4 py-2 rounded-sm"
            >
              Reload page
            </button>
          </div>
        </div>
      );
    }
    return this.props.children;
  }
}
